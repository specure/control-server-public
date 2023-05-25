package com.specure.service.core.impl;

import com.specure.common.config.MeasurementServerConfig;
import com.specure.common.enums.MeasurementStatus;
import com.specure.common.enums.Platform;
import com.specure.common.enums.ServerNetworkType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.dto.Profiling;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementDetails;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.service.digger.DiggerService;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.AbstractNamedRecord;
import com.maxmind.geoip2.record.Traits;
import com.specure.constant.MeasurementServerConstants;
import com.specure.mapper.core.MeasurementMapper;
import com.specure.model.dto.TimeSlot;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.service.admin.RawProviderService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.UserAgentExtractService;
import com.specure.utils.core.MeasurementCalculatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class MeasurementServiceImpl implements MeasurementService {

    public static final Integer TEST_DURATION_CONSTANT = 5;
    public static final boolean TEST_SERVER_ENCRYPTION = true;
    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;
    private final MeasurementServerConfig measurementServerConfig;
    private final DiggerService diggerService;
    private final UserAgentExtractService userAgentExtractService;
    private final MultiTenantManager multiTenantManager;
    private final FieldAnonymizerFilter fieldAnonymizerFilter;
    private final RawProviderService providerService;

    @Override
    public Measurement save(Measurement measurement) {
        log.info("MeasurementServiceImpl:save started with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        Measurement savedMeasurement = measurementRepository.save(measurement);
        log.debug("MeasurementServiceImpl:finished with tenant = {}, savedMeasurement = {}", multiTenantManager.getCurrentTenant(), savedMeasurement);
        return savedMeasurement;
    }

    @Override
    public Measurement partialUpdateMeasurementFromProbeResult(MeasurementRequest measurementRequest, Map<String, String> headers) {
        log.info("MeasurementServiceImpl:partialUpdateMeasurementFromProbeResult started with tenant = {}, request = {}", multiTenantManager.getCurrentTenant(), measurementRequest);
        var afterMeasure = measurementMapper.measurementRequestToMeasurement(measurementRequest);

        var findByTokenProfiling = new Profiling();
        findByTokenProfiling.start();
        var registeredMeasurement = measurementRepository
                .findByToken(afterMeasure.getToken())
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(afterMeasure.getToken()));
        log.debug("profiling: findByToken {}, tenant = {}", findByTokenProfiling.finishAndGetDuration(), multiTenantManager.getCurrentTenant());
        String clientIpAddress = HeaderExtrudeUtil.getIpFromNgNixHeader(headers);
        fieldAnonymizerFilter.refreshAnonymizedIpAddress(clientIpAddress, registeredMeasurement.getOpenTestUuid());

        Long id = registeredMeasurement.getId();

        afterMeasure.setId(id);
        afterMeasure.setOpenTestUuid(registeredMeasurement.getOpenTestUuid());
        afterMeasure.setClientUuid(registeredMeasurement.getClientUuid());
        afterMeasure.setNetworkOperator(registeredMeasurement.getNetworkOperator());
        afterMeasure.setDevice(registeredMeasurement.getDevice());
        afterMeasure.setBrowserName(registeredMeasurement.getBrowserName());
        afterMeasure.setTag(registeredMeasurement.getTag());
        afterMeasure.setIpAddress(registeredMeasurement.getIpAddress());
        afterMeasure.setToken(registeredMeasurement.getToken());
        afterMeasure.setTestSlot(registeredMeasurement.getTestSlot());
        afterMeasure.setTime(registeredMeasurement.getTime());
        afterMeasure.setMeasurementServerId(registeredMeasurement.getMeasurementServerId());
        afterMeasure.setServerType(registeredMeasurement.getServerType());
        afterMeasure.setClientProvider(registeredMeasurement.getClientProvider());
        afterMeasure.setStatus(MeasurementStatus.FINISHED);
        afterMeasure.setAdHocCampaign(registeredMeasurement.getAdHocCampaign());
        afterMeasure.setMeasurementDetails(registeredMeasurement.getMeasurementDetails());
        afterMeasure.setIspRawId(registeredMeasurement.getIspRawId());
        afterMeasure.setIspName(registeredMeasurement.getIspName());
        afterMeasure.setAsn(registeredMeasurement.getAsn());
        afterMeasure.setIsMno(registeredMeasurement.getIsMno());
        afterMeasure.setLatitude(registeredMeasurement.getLatitude());
        afterMeasure.setLongitude(registeredMeasurement.getLongitude());

        if (afterMeasure.getPings() != null) {
            afterMeasure.getPings().forEach(ping -> ping.setMeasurement(afterMeasure));
        }
        if (afterMeasure.getGeoLocations() != null) {
            afterMeasure.getGeoLocations().forEach(geoLocation -> geoLocation.setMeasurement(afterMeasure));
        }
        if (afterMeasure.getSpeedDetail() != null) {
            afterMeasure.getSpeedDetail().forEach(speedDetail -> speedDetail.setMeasurement(afterMeasure));
        }

        var saveToDbProfiling = new Profiling();
        saveToDbProfiling.start();
        var tenant = multiTenantManager.getCurrentTenant();
        System.out.println("main thread tenant = " + tenant);
        saveAsync(afterMeasure, tenant);
        log.debug("profiling: save to DB {} ", saveToDbProfiling.finishAndGetDuration());
        log.debug("MeasurementServiceImpl:partialUpdateMeasurementFromProbeResult finished with afterMeasure = {}", afterMeasure);
        return afterMeasure;
    }

    public void saveAsync(Measurement afterMeasure, String tenant) {
        log.trace("MeasurementServiceImpl:saveAsync started with tenant = {}, measurement = {}", tenant, afterMeasure);
        var one = new Thread(() -> {

            log.trace("separate thread tenant BEFORE = " + multiTenantManager.getCurrentTenant());
            multiTenantManager.setCurrentTenant(tenant);
            log.trace("separate thread tenant AFTER = " + multiTenantManager.getCurrentTenant());

            log.trace("profiling: ASYNC id = " + afterMeasure.getId());
            log.trace("profiling: ASYNC speed details size = " + afterMeasure.getSpeedDetail().size());
            measurementRepository.save(afterMeasure);
            log.trace("profiling: ASYNC saved");
        });
        one.start();
        log.trace("MeasurementServiceImpl:saveAsync finished successfully");
    }

    @Override
    public MeasurementRegistrationResponse registerMeasurement(DataForMeasurementRegistration dataForMeasurementRegistration, Map<String, String> headers) {
        var measurementServer = dataForMeasurementRegistration.getMeasurementServer();
        String ip = HeaderExtrudeUtil.getIpFromNgNixHeader(headers);
        log.info("MeasurementServiceImpl:registerMeasurement with tenant {},  dataForMeasurementRegistration = {}, ip = {}", multiTenantManager.getCurrentTenant(), dataForMeasurementRegistration, ip);
        log.debug("MeasurementServiceImpl:registerMeasurement tenant = {}, headers = {}", multiTenantManager.getCurrentTenant(), StringUtils.join(headers));

        var timeSlot = getTimeSlot(Instant.now().toEpochMilli());
        String testUuid = MeasurementCalculatorUtil.getUuid();
        String testToken = MeasurementCalculatorUtil.getToken(measurementServer.getSecretKey(), testUuid, timeSlot.getSlot());
        String device = dataForMeasurementRegistration.getDeviceOrProbeId();// /testRequest only
        String browserName = null;
        if (device == null) {
            String userAgentHeader = HeaderExtrudeUtil.getUserAgent(headers);
            browserName = userAgentExtractService.getBrowser(userAgentHeader);
        }

        var measurement = Measurement.builder()
                .measurementDetails(new MeasurementDetails())
                .openTestUuid(testUuid)
                .clientUuid(dataForMeasurementRegistration.getClientUuid())
                .adHocCampaign(dataForMeasurementRegistration.getAdHocCampaign())
                .measurementServerId(measurementServer.getId())
                .device(device)
                .browserName(browserName)
                .tag(dataForMeasurementRegistration.getPort())
                .token(testToken)
                .testSlot(timeSlot.getSlot())
                .time(new Timestamp(System.currentTimeMillis()))
                .ipAddress(fieldAnonymizerFilter.saveIpAddressFilter(ip, testUuid))
                .status(MeasurementStatus.STARTED)
                .platform(Optional.ofNullable(dataForMeasurementRegistration.getPlatform()).orElse(Platform.UNKNOWN))
                .longitude(dataForMeasurementRegistration.getLongitude())
                .latitude(dataForMeasurementRegistration.getLatitude())
                .build();
        measurement.getMeasurementDetails().setMeasurement(measurement);
        measurement.getMeasurementDetails().setAppVersion(dataForMeasurementRegistration.getAppVersion());
        measurement.getMeasurementDetails().setTelephonyPermissionGranted(dataForMeasurementRegistration.getTelephonyPermissionGranted());
        measurement.getMeasurementDetails().setUuidPermissionGranted(dataForMeasurementRegistration.getUuidPermissionGranted());
        measurement.getMeasurementDetails().setLocationPermissionGranted(dataForMeasurementRegistration.getLocationPermissionGranted());

        if (dataForMeasurementRegistration.getIsOnNet() != null) {
            ServerNetworkType serverNetworkType = dataForMeasurementRegistration.getIsOnNet() ? ServerNetworkType.ON_NET : ServerNetworkType.OFF_NET;
            measurement.setServerType(serverNetworkType.toString());
        }

        Optional<CityResponse> cityResponse = diggerService.getCityResponseByIpAddress(ip);
        Long asn = cityResponse
                .map(CityResponse::getTraits)
                .map(Traits::getAutonomousSystemNumber)
                .orElseGet(() -> diggerService.digASN(ip));
        cityResponse
                .ifPresent(x -> {
                    setCityDetailsFromIpAddress(measurement, x);
                    RawProvider rawProvider = providerService.getRawProvider(x, asn);
                    if (Objects.isNull(dataForMeasurementRegistration.getPort())) {
                        measurementMapper.updateMeasurementProviderInfo(measurement, rawProvider);
                    } else {
                        measurement.setIspRawId(rawProvider.getRawName());
                    }

                });
        if (Objects.nonNull(dataForMeasurementRegistration.getPort())) {
            // for /testRequest only
            measurement.setIspName(dataForMeasurementRegistration.getProviderName());
            //TODO: remove legacy network operator and clientProvider
            measurement.setNetworkOperator(measurement.getIspName());
        }
        //TODO: remove legacy network operator and clientProvider
        measurement.setClientProvider(measurement.getIspName());

        var savedMeasurement = measurementRepository.save(measurement);
        log.trace("MeasurementServiceImpl:registerMeasurement tenant = {}, savedMeasurement = {}", multiTenantManager.getCurrentTenant(), savedMeasurement);
        Integer port = dataForMeasurementRegistration.getMeasurementServerType().getServerTechForMeasurement().getDefaultSslPort();

        MeasurementRegistrationResponse measurementRegistrationResponse = MeasurementRegistrationResponse.builder()
                .testNumThreads(MeasurementServerConstants.TEST_NUM_THREADS)
                .testNumPings(MeasurementServerConstants.TEST_NUM_PINGS)
                .testDuration(TEST_DURATION_CONSTANT)
                .testId(savedMeasurement.getId())
                .testWait(timeSlot.getTestWait())
                .testUuid(testUuid)
                .testToken(testToken)
                .testServerPort(port)
                .testServerEncryption(TEST_SERVER_ENCRYPTION)
                .resultUrl(String.format(MeasurementServerConstants.RESULT_URL, measurementServerConfig.getHost()))
                .resultQosUrl(String.format(MeasurementServerConstants.RESULT_QOS_URL, measurementServerConfig.getHost()))
                .testServerName(measurementServer.getName())
                .testServerAddress(measurementServer.getWebAddress())
                .clientRemoteIp(ip)
                .provider(savedMeasurement.getIspName())
                .appVersion(savedMeasurement.getMeasurementDetails().getAppVersion())
                .platform(savedMeasurement.getPlatform())
                .error(Collections.emptyList())
                .build();
        log.debug("MeasurementServiceImpl:registerMeasurement finished with tenant = {}, response  = {}", multiTenantManager.getCurrentTenant(), measurementRegistrationResponse);
        return measurementRegistrationResponse;
    }

    @Transactional
    public TimeSlot getTimeSlot(long now) {
        log.trace("MeasurementServiceImpl:getTimeSlot started with tenant = {}, time now = {}", multiTenantManager.getCurrentTenant(), now);
        var slot = Math.toIntExact(now / 1000);
        long counter = measurementRepository.countAllByTestSlot(slot);
        var wait = 0;
        long max = measurementServerConfig.getSlotWindow().intValue();

        while (counter >= max) {
            slot++;
            wait++;
            counter = measurementRepository.countAllByTestSlot(slot);
        }
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(slot)
                .testWait(wait)
                .build();
        log.trace("MeasurementServiceImpl:getTimeSlot finished with tenant = {}, time now = {}", multiTenantManager.getCurrentTenant(), now);
        return timeSlot;
    }

    @Override
    public MeasurementHistoryResponse getMeasurementDetailByUuid(String uuid) {
        log.debug("MeasurementServiceImpl:getMeasurementDetailByUuid started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        MeasurementHistoryResponse measurementHistoryResponse = measurementRepository
                .findByOpenTestUuid(uuid)
                .map(measurementMapper::measurementToMeasurementHistoryResponse)
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(uuid));
        log.debug("MeasurementServiceImpl:getMeasurementDetailByUuid finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), measurementHistoryResponse);
        return measurementHistoryResponse;
    }

    @Override
    public Measurement findByOpenTestUuid(String uuid) {
        log.debug("MeasurementServiceImpl:findByOpenTestUuid started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        Measurement measurement = measurementRepository.findByOpenTestUuid(uuid).orElseThrow(() -> new MeasurementNotFoundByUuidException(uuid));
        log.debug("MeasurementServiceImpl: finished with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        return measurement;
    }

    @Override
    public Optional<Measurement> getMeasurementByToken(String token) {
        log.debug("MeasurementServiceImpl:getMeasurementByToken started with tenant = {}, token = {}", multiTenantManager.getCurrentTenant(), token);
        Optional<Measurement> measurement = measurementRepository.findByToken(token);
        log.debug("MeasurementServiceImpl:getMeasurementByToken finished with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        return measurement;
    }

    @Override
    public Page<Measurement> findAll(Pageable pageable) {
        log.debug("MeasurementServiceImpl:findAll started with tenant = {}, pageable = {}", multiTenantManager.getCurrentTenant(), pageable);
        Page<Measurement> page = measurementRepository.findAll(pageable);
        log.debug("MeasurementServiceImpl:findAll finished with tenant = {}, page = {}", multiTenantManager.getCurrentTenant(), page);
        return page;
    }

    @Override
    public void setCityDetailsFromIpAddress(Measurement measurement, CityResponse cityResponse) {
        log.trace("BasicTestServiceImpl:saveToElastic tenant = {}, cityResponse = {}", multiTenantManager.getCurrentTenant(), cityResponse);
        if (cityResponse != null) {
            var city = cityResponse.getCity();
            var country = cityResponse.getCountry();
            var county = cityResponse.getSubdivisions().stream()
                    .map(AbstractNamedRecord::getName)
                    .collect(Collectors.joining(", "));
            var postalCode = cityResponse.getPostal();
            MeasurementDetails measurementDetails = measurement.getMeasurementDetails();

            if (country != null) {
                measurementDetails.setCountry(country.getName());
            }

            if (Boolean.TRUE.equals(measurement.getMeasurementDetails().getLocationPermissionGranted())
                    || Objects.isNull(measurement.getMeasurementDetails().getLocationPermissionGranted())) {
                if (city != null) {
                    measurementDetails.setCity(city.getName());
                }
                if (county.length() != 0) {
                    measurementDetails.setCounty(county);
                }
                if (postalCode != null) {
                    measurementDetails.setPostalCode(postalCode.getCode());
                }
            }
        }
    }
}

