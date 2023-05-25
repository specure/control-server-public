package com.specure.service.mobile.impl;


import com.google.common.collect.Sets;
import com.specure.common.model.jpa.*;
import com.specure.config.ApplicationProperties;
import com.specure.common.constant.AdminSetting;
import com.specure.constant.Constants;
import com.specure.common.constant.HeaderConstants;
import com.specure.constant.URIConstants;
import com.specure.common.enums.MeasurementStatus;
import com.specure.common.enums.Platform;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.service.core.*;
import com.specure.service.mobile.LoopModeSettingsService;
import com.specure.service.mobile.MobileMeasurementRegistrationService;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.specure.utils.core.MeasurementCalculatorUtil;
import com.specure.utils.sah.MessageUtils;
import com.vdurmont.semver4j.SemverException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MobileMeasurementRegistrationServiceImpl implements MobileMeasurementRegistrationService {

    private static final Set<String> TIMEZONES = Sets.newHashSet(TimeZone.getAvailableIDs());

    private final ClientService clientService;

    private final MeasurementService measurementService;

    private final ApplicationProperties applicationProperties;

    private final MessageSource messageSource;

    private final MultiTenantManager multiTenantManager;

    private final LoopModeSettingsService loopModeSettingsService;

    private final FieldAnonymizerFilter fieldAnonymizerFilter;

    private final SettingsService settingsService;

    @Qualifier("basicMeasurementServerService")
    private final MeasurementServerService measurementServerService;

    @Override
    @Transactional
    public MobileMeasurementRegistrationResponse registerMobileMeasurement(MobileMeasurementSettingRequest mobileMeasurementSettingRequest, Map<String, String> headers, HttpServletRequest request) {
        log.info("MobileMeasurementRegistrationResponse:registerMobileMeasurement started with tenant = {}, mobileMeasurementSettingRequest = {}", multiTenantManager.getCurrentTenant(), mobileMeasurementSettingRequest);
        log.debug("MobileMeasurementRegistrationResponse:registerMobileMeasurement tenant = {}, headers = {}", multiTenantManager.getCurrentTenant(), StringUtils.join(headers));
        String clientIpAddress = HeaderExtrudeUtil.getIpFromNgNixHeader(headers);

        MobileMeasurementRegistrationResponse.MobileMeasurementRegistrationResponseBuilder builder = MobileMeasurementRegistrationResponse.builder();
        List<String> errorResponse = new ArrayList<>();
        String language = MessageUtils.getSafeLanguage(mobileMeasurementSettingRequest.getLanguage(), applicationProperties.getLanguage());
        Locale locale = Locale.forLanguageTag(language);

        try {
            final String settingUuid = mobileMeasurementSettingRequest.getClientUuid();
            UUID clientUuid = getUuid(settingUuid);

            if (!TIMEZONES.contains(mobileMeasurementSettingRequest.getTimezone()))
                errorResponse.add(getErrorMessage("ERROR_TIMEZONE", locale));

            String timeZoneId = mobileMeasurementSettingRequest.getTimezone();
            Client client = getClientOrAddError(clientUuid, errorResponse, locale);

            if (client != null) {
                log.trace("MobileMeasurementRegistrationResponse:registerMobileMeasurement tenant = {}, client not null", multiTenantManager.getCurrentTenant());
                final UUID testOpenUuid = UUID.randomUUID();
                MeasurementServer measurementServer = measurementServerService.getMeasurementServerByIdOrGetDefault(mobileMeasurementSettingRequest.getPreferredServer());//id in request or default
                builder
                        .testServerAddress(measurementServer.getWebAddress())
                        .testServerName(measurementServer.getName())
                        .clientRemoteIp(clientIpAddress);
                Map<String, String> settingsMap = settingsService.getSettingsMap();
                setServerTypeDetails(builder, measurementServer, mobileMeasurementSettingRequest);
                String numberOfThreadsString = getTestNumThreads(mobileMeasurementSettingRequest, settingsMap);
                builder.testDuration(settingsMap.getOrDefault(AdminSetting.MEASUREMENT_DURATION_KEY, String.valueOf(applicationProperties.getDuration())))
                        .testNumThreadsMobile(numberOfThreadsString)
                        .testNumPings(settingsMap.getOrDefault(AdminSetting.MEASUREMENT_NUM_PINGS_KEY, String.valueOf(applicationProperties.getPings())));

                String resultUrl;
                if (request.getHeader(HeaderConstants.URL) != null) {
                    resultUrl = request.getHeader(HeaderConstants.URL);
                } else {
                    resultUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .build()
                            .toString();
                }
                builder.resultUrl(resultUrl + URIConstants.MOBILE + URIConstants.RESULT);
                builder.resultQosUrl(resultUrl + URIConstants.MOBILE + URIConstants.RESULT_QOS_URL);


                if (errorResponse.isEmpty()) {
                    Measurement test = getTest(mobileMeasurementSettingRequest, language, timeZoneId, client, testOpenUuid, Integer.valueOf(numberOfThreadsString), measurementServer, clientIpAddress);
                    Optional.ofNullable(mobileMeasurementSettingRequest.getLoopModeInfo())
                            .map(loopModeInfo -> loopModeSettingsService.processLoopModeSettingsInfo(clientUuid.toString(), loopModeInfo, testOpenUuid.toString()))
                            .ifPresent(builder::loopUuid);
                    setLocationDetails(test, mobileMeasurementSettingRequest.getLocation());
                    test = measurementService.save(test);
                    log.trace("MobileMeasurementRegistrationResponse:registerMobileMeasurement tenant = {}, savedTest = {}", multiTenantManager.getCurrentTenant(), test);
                    var timeSlot = measurementService.getTimeSlot(Instant.now().toEpochMilli());
                    if (timeSlot.getSlot() < 0) {
                        errorResponse.add(getErrorMessageAndRollback("ERROR_DB_STORE_GENERAL", locale));
                    } else {
                        final String token = MeasurementCalculatorUtil.getToken(measurementServer.getSecretKey(), testOpenUuid.toString(), timeSlot.getSlot());

                        test.setToken(token);
                        test.setTestSlot(timeSlot.getSlot());
                        test = measurementService.save(test);
                        log.trace("MobileMeasurementRegistrationResponse:registerMobileMeasurement tenant = {}, savedTest = {}", multiTenantManager.getCurrentTenant(), test);
//                        int waitTime = timeSlot.getSlot() - (int) (System.currentTimeMillis() / 1000);
                        builder.testToken(token)
                                .testUuid(testOpenUuid.toString())
                                .openTestUUID("O" + testOpenUuid)
                                .testId(test.getId())
                                .platform(test.getPlatform())
                                .appVersion(test.getMeasurementDetails().getAppVersion())
                                .testWait(timeSlot.getTestWait());
                    }
                }
            } else {
                errorResponse.add(getErrorMessageAndRollback("ERROR_CLIENT_UUID", locale));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            errorResponse.add(getErrorMessageAndRollback("ERROR_DB_CONNECTION", locale));
        } catch (SemverException e) {
            errorResponse.add(getErrorMessageAndRollback("ERROR_CLIENT_VERSION", locale));
            e.printStackTrace();
        }

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = builder
                .error(errorResponse)
                .build();
        log.debug("MobileMeasurementRegistrationResponse:registerMobileMeasurement finished with tenant = {}, mobileMeasurementRegistrationResponse = {}", multiTenantManager.getCurrentTenant(), mobileMeasurementRegistrationResponse);
        return mobileMeasurementRegistrationResponse;
    }

    private void setServerTypeDetails(MobileMeasurementRegistrationResponse.MobileMeasurementRegistrationResponseBuilder builder, MeasurementServer measurementServer, MobileMeasurementSettingRequest mobileMeasurementSettingRequest) {
        Optional<MeasurementServerTypeDetail> measurementServerTypeDetail = getMeasurementServerTypeDetail(measurementServer, mobileMeasurementSettingRequest);
        measurementServerTypeDetail.ifPresentOrElse((MeasurementServerTypeDetail m) -> {
            builder.testServerEncryption(m.isEncrypted());
            builder.testServerType(m.getServerType());
            if (m.isEncrypted()) {
                builder.testServerPort(m.getPortSsl());
            } else {
                builder.testServerPort(m.getPort());
            }
        }, () -> {
            //TODO: deprecated
            builder.testServerEncryption(true);
            builder.testServerPort(measurementServer.getPort());
            builder.testServerType(Optional.ofNullable(measurementServer.getServerType())
                    .orElse(Constants.DEFAULT_MEASUREMENT_SERVER_TYPE));
        });
    }

    private Optional<MeasurementServerTypeDetail> getMeasurementServerTypeDetail(MeasurementServer measurementServer, MobileMeasurementSettingRequest mobileMeasurementSettingRequest) {
        return CollectionUtils.emptyIfNull(measurementServer.getServerTypeDetails())
                .stream()
                .filter(x -> x.getServerType().equals(mobileMeasurementSettingRequest.getMeasurementServerType()))
                .findFirst();
    }

    private void setLocationDetails(Measurement measurement, LocationRequest location) {
        MeasurementDetails measurementDetails = measurement.getMeasurementDetails();
        if (Objects.nonNull(location)) {
            measurementDetails.setCountry(location.getCountry());
            if (Boolean.TRUE.equals(measurement.getMeasurementDetails().getLocationPermissionGranted())
                    || Objects.isNull(measurement.getMeasurementDetails().getLocationPermissionGranted())) {
                measurementDetails.setCity(location.getCity());
                measurementDetails.setCounty(location.getCounty());
                measurementDetails.setPostalCode(location.getPostalCode());
                measurement.setLatitude(location.getLatitude());
                measurement.setLongitude(location.getLongitude());
            }
        }
        measurementDetails.setMeasurement(measurement);
    }

    private String getTestNumThreads(MobileMeasurementSettingRequest mobileMeasurementSettingRequest, Map<String, String> settingsMap) {
        String numThreadsKeyByPlatform = getNumberOfThreadsKeyByPlatform(mobileMeasurementSettingRequest.getPlatform());
        return Optional.ofNullable(mobileMeasurementSettingRequest.getNumberOfThreads())
                .map(String::valueOf)
                .orElse(settingsMap.getOrDefault(numThreadsKeyByPlatform, String.valueOf(applicationProperties.getThreads())));
    }

    private String getNumberOfThreadsKeyByPlatform(Platform platform) {
        if (Objects.nonNull(platform) && platform.equals(Platform.IOS)) {
            return AdminSetting.MEASUREMENT_NUM_THREADS_IOS_KEY;
        }
        return AdminSetting.MEASUREMENT_NUM_THREADS_ANDROID_KEY;
    }

    private UUID getUuid(String settingUuid) {
        if (StringUtils.isNotBlank(settingUuid)) {
            if (settingUuid.startsWith("U") && settingUuid.length() > 1)
                return UUID.fromString(settingUuid.substring(1));
            else return UUID.fromString(settingUuid);
        }
        return null;
    }

    private Measurement getTest(MobileMeasurementSettingRequest mobileMeasurementSettingRequest,
                                String language,
                                String timeZoneId,
                                Client client,
                                UUID testOpenUuid,
                                Integer numberOfThreads,
                                MeasurementServer measurementServer,
                                String clientIpAddress) {
        log.trace("MobileMeasurementRegistrationResponse:getTest started with tenant = {}, " +
                        "mobileMeasurementSettingRequest = {}, language = {}, timeZoneId = {}, client = {}, testOpenUuid = {}, " +
                        "numberOfThreads = {}, measurementServer = {}, clientIpAddress = {}",
                multiTenantManager.getCurrentTenant(), mobileMeasurementSettingRequest, language, timeZoneId, client, testOpenUuid,
                numberOfThreads, measurementServer, clientIpAddress);
        Measurement measurement = new Measurement();
        measurement.setOpenTestUuid(testOpenUuid.toString());
        measurement.setClientUuid(client.getUuid());
        measurement.setClientName(mobileMeasurementSettingRequest.getClientName());
        measurement.setClientVersion(mobileMeasurementSettingRequest.getClientVersion());
        measurement.setClientLanguage(language);
        measurement.setMeasurementServerId(measurementServer.getId());
        measurement.setTimezone(timeZoneId);
        measurement.setTestNumThreads(numberOfThreads);
        measurement.setStatus(MeasurementStatus.STARTED);
        measurement.setType(mobileMeasurementSettingRequest.getMeasurementType().toString());
        measurement.setIpAddress(fieldAnonymizerFilter.saveIpAddressFilter(clientIpAddress, testOpenUuid.toString()));
        measurement.setMeasurementDetails(new MeasurementDetails());
        measurement.getMeasurementDetails().setLocationPermissionGranted(mobileMeasurementSettingRequest.getLocationPermissionGranted());
        measurement.getMeasurementDetails().setUuidPermissionGranted(mobileMeasurementSettingRequest.getUuidPermissionGranted());
        measurement.getMeasurementDetails().setTelephonyPermissionGranted(mobileMeasurementSettingRequest.getTelephonyPermissionGranted());
        measurement.getMeasurementDetails().setAppVersion(mobileMeasurementSettingRequest.getAppVersion());
        measurement.setPlatform(Optional.ofNullable(mobileMeasurementSettingRequest.getPlatform()).orElse(Platform.UNKNOWN));
        log.trace("MobileMeasurementRegistrationResponse:getTest finished with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        return measurement;
    }

    private Client getClientOrAddError(UUID uuid, List<String> errorResponse, Locale locale) {
        log.trace("MobileMeasurementRegistrationResponse:getClientOrAddError started with tenant = {}, uuid = {}, errorResponse = {}, locale = {}",
                multiTenantManager.getCurrentTenant(), uuid, errorResponse, locale);
        Client client = null;
        if (uuid != null) {
            client = clientService.getClientByUUID(uuid);

            if (client == null)
                errorResponse.add(getErrorMessageAndRollback("ERROR_DB_GET_CLIENT", locale));
        }
        log.trace("MobileMeasurementRegistrationResponse:getClientOrAddError finished with tenant = {}, client = {}, errorResponse = {}",
                multiTenantManager.getCurrentTenant(), client, errorResponse);
        return client;
    }

    private String getErrorMessageAndRollback(String key, Locale locale) {
        log.trace("MobileMeasurementRegistrationResponse:getErrorMessageAndRollback started with tenant = {}, key = {}, locale = {}", multiTenantManager.getCurrentTenant(), key, locale);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        String errorMessage = getErrorMessage(key, locale);
        log.trace("MobileMeasurementRegistrationResponse:getErrorMessageAndRollback finished with tenant = {}, errorMessage = {}", multiTenantManager.getCurrentTenant(), errorMessage);
        return errorMessage;
    }

    private String getErrorMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
