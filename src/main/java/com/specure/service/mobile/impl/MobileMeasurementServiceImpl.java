package com.specure.service.mobile.impl;

import com.specure.common.constant.AdminSetting;
import com.specure.common.enums.NetworkType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.common.service.digger.DiggerService;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.maxmind.geoip2.model.AbstractCountryResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Traits;
import com.specure.config.ApplicationProperties;
import com.specure.constant.Constants;
import com.specure.constant.ErrorMessage;
import com.specure.dto.mobile.QoeClassificationThresholds;
import com.specure.enums.QoeCategory;
import com.specure.mapper.core.MeasurementMapper;
import com.specure.mapper.mobile.MobileMeasurementMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.CapabilitiesRequest;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementResultDetailRequest;
import com.specure.request.mobile.MobileMeasurementResultRequest;
import com.specure.response.mobile.*;
import com.specure.service.admin.RawProviderService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.SettingsService;
import com.specure.service.mobile.MobileMeasurementService;
import com.specure.service.sah.BasicTestService;
import com.specure.utils.mobile.ClassificationUtils;
import com.specure.utils.sah.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MobileMeasurementServiceImpl implements MobileMeasurementService {

    private final ApplicationProperties applicationProperties;
    private final MessageSource messageSource;
    private final MeasurementServerRepository measurementServerRepository;
    private final MeasurementRepository measurementRepository;
    private final MobileMeasurementMapper mobileMeasurementMapper;
    @Qualifier("sahBasicTestService")
    private final BasicTestService sahBasicTestService;
    private final MultiTenantManager multiTenantManager;
    private final RawProviderService rawProviderService;
    private final DiggerService diggerService;
    private final MeasurementMapper measurementMapper;
    private final SettingsService settingsService;
    private final FieldAnonymizerFilter fieldAnonymizerFilter;

    @SneakyThrows
    @Override
    public MeasurementResultMobileResponse processMeasurementResult(MeasurementResultMobileRequest measurementResultMobileRequest, Map<String, String> headers) {
        log.info("MeasurementResultMobileResponse:processMeasurementResult started with tenant = {}, measurementResultMobileRequest = {} ", multiTenantManager.getCurrentTenant(), measurementResultMobileRequest);
        String requestUUID = UUID.fromString(measurementResultMobileRequest.getTestToken().split("_")[0]).toString();
        String clientIpAddress = HeaderExtrudeUtil.getIpFromNgNixHeader(headers);
        fieldAnonymizerFilter.refreshAnonymizedIpAddress(clientIpAddress, requestUUID);
        Measurement measurement = measurementRepository.findByOpenTestUuid(requestUUID)
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(requestUUID));

        final String simRawProviderCountry = measurementResultMobileRequest.getSimCountry();
        final String simRawProviderName = measurementResultMobileRequest.getSimOperatorName();
        final String simRawProviderMccMnc = measurementResultMobileRequest.getSimMccMnc();

        Optional<CityResponse> cityResponse = null;
        String networkMccMnc;
        if (Objects.nonNull((measurementResultMobileRequest.getNetworkMccMnc()))) {
            networkMccMnc = measurementResultMobileRequest.getNetworkMccMnc();
        } else {
            cityResponse = diggerService.getCityResponseByIpAddress(clientIpAddress);
            networkMccMnc = cityResponse
                    .map(AbstractCountryResponse::getTraits)
                    .filter(x -> Objects.nonNull(x.getMobileCountryCode()) && Objects.nonNull(x.getMobileNetworkCode()))
                    .map(x -> String.join(com.specure.common.constant.Constants.MCC_MNC_JOINER, x.getMobileCountryCode(), x.getMobileNetworkCode()))
                    .orElse(null);
        }

        measurement.setNetworkMccMnc(networkMccMnc);
        if (Objects.nonNull(simRawProviderCountry) && Objects.nonNull(simRawProviderName)) {
            RawProvider rawProvider = rawProviderService.getRawProvider(simRawProviderCountry, simRawProviderName, simRawProviderMccMnc);
            measurementMapper.updateMeasurementProviderInfo(measurement, rawProvider);
            measurement.setNetworkOperator(simRawProviderName);

        } else {
            if (Objects.isNull(cityResponse)) {
                cityResponse = diggerService.getCityResponseByIpAddress(clientIpAddress);
            }
            Long asn = cityResponse
                    .map(CityResponse::getTraits)
                    .map(Traits::getAutonomousSystemNumber)
                    .orElseGet(() -> diggerService.digASN(clientIpAddress));

            String clientProviderName = cityResponse
                    .map(AbstractCountryResponse::getTraits)
                    .map(Traits::getIsp)
                    .or(() -> Optional.ofNullable(diggerService.getProviderByASN(asn)))
                    .orElse(ErrorMessage.UNKNOWN_PROVIDER);
            measurement.setNetworkOperator(clientProviderName);
            cityResponse
                    .ifPresent(x ->
                    {
                        RawProvider rawProvider = rawProviderService.getRawProvider(x, asn, measurementResultMobileRequest.getNetworkMccMnc());
                        measurementMapper.updateMeasurementProviderInfo(measurement, rawProvider);

                    });
        }

        Measurement updatedMeasurement = mobileMeasurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement);
        sahBasicTestService.saveMeasurementMobileToElastic(updatedMeasurement);

        saveToWriteNode(updatedMeasurement, multiTenantManager.getCurrentTenant());
        Thread.sleep(2500); //to test possible fix
        return MeasurementResultMobileResponse.builder()
                .error(new ArrayList<>())
                .build();
    }

    public void saveToWriteNode(Measurement afterMeasure, String tenant) {
        log.trace("MeasurementResultMobileResponse:saveToWriteNode started with tenant = {}, measurement = {}", tenant, afterMeasure);
        var one = new Thread(() -> {
            multiTenantManager.setCurrentTenant(tenant);
            measurementRepository.save(afterMeasure);
        });
        one.start();
        log.trace("MeasurementResultMobileResponse:saveToWriteNode finished with tenant = {}", tenant);
    }

    @Override
    public MobileMeasurementResultContainerResponse getTestResult(MobileMeasurementResultRequest mobileMeasurementResultRequest) {
        log.debug("MobileMeasurementResultContainerResponse:getTestResult with tenant = {}, mobileMeasurementResultRequest = {} ", multiTenantManager.getCurrentTenant(), mobileMeasurementResultRequest.toString());
        BasicTest test = sahBasicTestService.getBasicTestByUUID(mobileMeasurementResultRequest.getTestUUID().toString());
        Locale locale = MessageUtils.getLocaleFormLanguage(mobileMeasurementResultRequest.getLanguage(), applicationProperties.getLanguage());
        String timeString = TimeUtils.getTimeStringFromTest(test, locale);
        MobileMeasurementResultResponse.MobileMeasurementResultResponseBuilder testResultResponseBuilder = MobileMeasurementResultResponse.builder()
                .time(test.getTimestamp().getTime())
                .timezone(TimeUtils.getTimeZoneFromBasicTest(test).getDisplayName())
                .measurementResult(getMeasurementResult(test, mobileMeasurementResultRequest.getCapabilitiesRequest()))
                .measurement(getMeasurements(test, locale, mobileMeasurementResultRequest.getCapabilitiesRequest()))
                .openTestUUID(String.format(Constants.TEST_RESULT_DETAIL_OPEN_TEST_UUID_TEMPLATE, test.getOpenTestUuid()))
                .openUUID(String.format(Constants.TEST_RESULT_DETAIL_OPEN_UUID_TEMPLATE, test.getOpenTestUuid()))
                .shareSubject(MessageFormat.format(getStringFromBundle("RESULT_SHARE_SUBJECT", locale), timeString))
                .shareText(getShareText(test, timeString, locale))
                .timeString(timeString)
                .qoeClassificationResponses(getQoeClassificationResponse(test));
        setNetFields(testResultResponseBuilder, test, locale);
        MobileMeasurementResultContainerResponse mobileMeasurementResultContainerResponse = MobileMeasurementResultContainerResponse.builder()
                .mobileMeasurementResultRespons(List.of(testResultResponseBuilder.build()))
                .build();
        log.debug("MobileMeasurementResultContainerResponse:getTestResult with tenant = {}, mobileMeasurementResultContainerResponse = {}", multiTenantManager.getCurrentTenant(), mobileMeasurementResultContainerResponse);
        return mobileMeasurementResultContainerResponse;
    }

    private List<QoeClassificationResponse> getQoeClassificationResponse(BasicTest test) {
        log.trace("MobileMeasurementResultContainerResponse:getQoeClassificationResponse started with tenant = {}, basicTest = {}", multiTenantManager.getCurrentTenant(), test);
        long pingNs = Optional.ofNullable(test.getPing())
                .map(t -> t * com.specure.common.constant.Constants.PING_CONVERSION_MULTIPLICATOR)
                .map(Double::longValue)
                .orElse(NumberUtils.LONG_ZERO);
        long downKBps = Optional.ofNullable(test.getDownload())
                .map(Integer::longValue)
                .orElse(NumberUtils.LONG_ZERO);
        long upKbps = Optional.ofNullable(test.getUpload())
                .map(Integer::longValue)
                .orElse(NumberUtils.LONG_ZERO);
        List<QoeClassificationThresholds> qoeClassificationThresholds = QoeCategory.getQoeClassificationThreshold();
        List<QoeClassificationResponse> qoeClassificationResponses = ClassificationUtils.classify(pingNs, downKBps, upKbps, qoeClassificationThresholds);
        log.trace("MobileMeasurementResultContainerResponse:getQoeClassificationResponse finished with tenant = {}, qoeClassificationResponses = {}", multiTenantManager.getCurrentTenant(), qoeClassificationResponses);
        return qoeClassificationResponses;
    }

    @Override
    public MobileMeasurementResultDetailResponse getTestResultDetailByTestUUID(MobileMeasurementResultDetailRequest mobileMeasurementResultDetailRequest) {
        log.debug("MobileMeasurementResultDetailResponse:getTestResultDetailByTestUUID started with tenant = {}, mobileMeasurementResultDetailRequest =  {}", multiTenantManager.getCurrentTenant(), mobileMeasurementResultDetailRequest);
        final Locale locale = MessageUtils.getLocaleFormLanguage(mobileMeasurementResultDetailRequest.getLanguage(), applicationProperties.getLanguage());
        List<MobileMeasurementResultDetailContainerResponse> propertiesList = new ArrayList<>();
        BasicTest test = sahBasicTestService.getBasicTestByUUID(mobileMeasurementResultDetailRequest.getTestUUID().toString());
        addOpenTestUUID(propertiesList, test, locale);
        addTime(propertiesList, test, locale);
        addTestFields(propertiesList, locale, test);

        MobileMeasurementResultDetailResponse mobileMeasurementResultDetailResponse = MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(propertiesList)
                .build();
        log.debug("MobileMeasurementResultDetailResponse:getTestResultDetailByTestUUID finished with tenant = {}, mobileMeasurementResultDetailResponse = {}", multiTenantManager.getCurrentTenant(), mobileMeasurementResultDetailResponse);
        return mobileMeasurementResultDetailResponse;
    }

    private void addOpenTestUUID(List<MobileMeasurementResultDetailContainerResponse> propertiesList, BasicTest test, Locale locale) {
        MobileMeasurementResultDetailContainerResponse timeResponse = MobileMeasurementResultDetailContainerResponse.builder()
                .title("Open Test ID")
                .value(String.format(Constants.TEST_RESULT_DETAIL_OPEN_TEST_UUID_TEMPLATE, test.getOpenTestUuid()))
                .openTestUUID(String.format(Constants.TEST_RESULT_DETAIL_OPEN_TEST_UUID_TEMPLATE, test.getOpenTestUuid()))
                .build();
        propertiesList.add(timeResponse);
    }

    private void addTime(List<MobileMeasurementResultDetailContainerResponse> propertiesList, BasicTest test, Locale locale) {
        if (Objects.nonNull(test.getTimestamp()) && Objects.nonNull(test.getMeasurementDate())) {
            Date date = Date.from(test.getTimestamp().toInstant());
            TimeZone timeZone = TimeUtils.getTimeZoneFromBasicTest(test);
            MobileMeasurementResultDetailContainerResponse timeResponse = buildTimeResponse(test, date, timeZone, locale);
            MobileMeasurementResultDetailContainerResponse timezoneResponse = buildTimezoneResponse(timeZone, date.getTime(), locale);
            propertiesList.add(timeResponse);
            propertiesList.add(timezoneResponse);
        }
    }

    private MobileMeasurementResultDetailContainerResponse buildTimezoneResponse(TimeZone timeZone, long time, Locale locale) {
        return MobileMeasurementResultDetailContainerResponse.builder()
                .title(getStringFromBundleWithKeyPrefix("timezone", locale))
                .value(getTimezoneValue(timeZone, time, locale))
                .build();
    }

    private MobileMeasurementResultDetailContainerResponse buildTimeResponse(BasicTest test, Date date, TimeZone timeZone, Locale locale) {
        return MobileMeasurementResultDetailContainerResponse.builder()
                .time(date.getTime())
                .timezone(TimeUtils.getTimeZoneFromBasicTest(test).getID())
                .title(getStringFromBundleWithKeyPrefix("time", locale))
                .value(TimeUtils.getTimeString(date, timeZone, locale))
                .build();
    }

    private String getTimezoneValue(TimeZone timezone, long time, Locale locale) {
        Format timeZoneFormat = new DecimalFormat(Constants.TIMEZONE_PATTERN, new DecimalFormatSymbols(locale));
        double offset = timezone.getOffset(time) / Constants.MILLISECONDS_TO_HOURS;
        return String.format(Constants.TIMEZONE_TEMPLATE, timeZoneFormat.format(offset));
    }

    private void addTestFields(List<MobileMeasurementResultDetailContainerResponse> propertiesList, Locale locale, BasicTest test) {
        if (!MeasurementUtils.isDualSim(NetworkType.valueOfSafely(test.getNetworkType()).getValue(), test.getDualSim())) {
            addIntegerAndUnitString(propertiesList, locale, "signal_strength", test.getSignal(), "RESULT_SIGNAL_UNIT");
            addIntegerAndUnitString(propertiesList, locale, "signal_rsrp", test.getLte_rsrp(), "RESULT_SIGNAL_UNIT");
            addIntegerAndUnitString(propertiesList, locale, "signal_rsrq", test.getLte_rsrq(), "RESULT_DB_UNIT");
            Optional.ofNullable(test.getNetworkType())
                    .map(NetworkType::valueOfSafely)
                    .map(NetworkType::getValue)
                    .map(HelperFunctions::getNetworkTypeName)
                    .ifPresent(networkType -> addString(propertiesList, locale, "network_type", networkType));
        }
        Optional.ofNullable(test.getDownload())
                .map(x -> x / com.specure.common.constant.Constants.BYTES_UNIT_CONVERSION_MULTIPLICATOR)
                .ifPresent(downloadSpeed -> addDoubleAndUnitString(propertiesList, locale, "speed_download", downloadSpeed, "RESULT_DOWNLOAD_UNIT"));
        Optional.ofNullable(test.getUpload())
                .map(x -> x / com.specure.common.constant.Constants.BYTES_UNIT_CONVERSION_MULTIPLICATOR)
                .ifPresent(uploadSpeed -> addDoubleAndUnitString(propertiesList, locale, "speed_upload", uploadSpeed, "RESULT_UPLOAD_UNIT"));
        Optional.ofNullable(test.getPing())
                .map(Float::doubleValue)
                .ifPresent(pingMedian -> addDoubleAndUnitString(propertiesList, locale, "ping_median", pingMedian, "RESULT_PING_UNIT"));
        addString(propertiesList, locale, "wifi_ssid", fieldAnonymizerFilter.getWifiSsidFilter(test.getOpenTestUuid()));
        Optional.ofNullable(test.getMeasurementServerId())
                .map(measurementServerRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MeasurementServer::getName)
                .ifPresent(testServerName -> addString(propertiesList, locale, "server_name", testServerName));
        Optional.ofNullable(test.getPlatform())
                .ifPresent(platformLabel -> addString(propertiesList, locale, "plattform", platformLabel));
        addString(propertiesList, locale, "model", test.getDevice());
        Optional.ofNullable(test.getClientName())
                .ifPresent(serverTypeLabel -> addString(propertiesList, locale, "client_name", serverTypeLabel));
        addString(propertiesList, locale, "client_version", test.getClientVersion());
        addInteger(propertiesList, locale, "num_threads", test.getNumThreads());
        addInteger(propertiesList, locale, "num_threads_ul", test.getTestNumThreadsUl());
        addString(propertiesList, locale, "tag", test.getTag());
    }

    private void addInteger(List<MobileMeasurementResultDetailContainerResponse> propertiesList, Locale locale, String key, Integer value) {
        if (value != null) {
            MobileMeasurementResultDetailContainerResponse newItem = MobileMeasurementResultDetailContainerResponse.builder()
                    .title(getStringFromBundleWithKeyPrefix(key, locale))
                    .value(value.toString())
                    .build();
            propertiesList.add(newItem);
        }
    }

    private void addDoubleAndUnitString(List<MobileMeasurementResultDetailContainerResponse> propertiesList, Locale locale, String title, Double value, String unitKey) {
        String unit = getStringFromBundle(unitKey, locale);
        if (Objects.nonNull(value)) {
            addString(propertiesList, locale, title, FormatUtils.formatValueAndUnit(value, unit, locale));
        }
    }

    private void addIntegerAndUnitString(List<MobileMeasurementResultDetailContainerResponse> propertiesList, Locale locale, String title, Integer value, String unitKey) {
        String unit = getStringFromBundle(unitKey, locale);
        if (Objects.nonNull(value)) {
            addString(propertiesList, locale, title, FormatUtils.formatValueAndUnit(value, unit));
        }
    }

    private String getStringFromBundle(String value, Locale locale) {
        try {
            return messageSource.getMessage(value, null, locale);
        } catch (final MissingResourceException e) {
            return value;
        }
    }

    private String getStringFromBundleWithKeyPrefix(String key, Locale locale) {
        try {
            return messageSource.getMessage("key_" + key, null, locale);
        } catch (final MissingResourceException e) {
            return key;
        }
    }

    private void addString(List<MobileMeasurementResultDetailContainerResponse> propertiesList, Locale locale, String key, String value) {
        if (value != null && !value.isEmpty()) {
            MobileMeasurementResultDetailContainerResponse newItem = MobileMeasurementResultDetailContainerResponse.builder()
                    .title(getStringFromBundleWithKeyPrefix(key, locale))
                    .value(value)
                    .build();
            propertiesList.add(newItem);
        }
    }

    private List<MobileMeasurementResultMeasurementResponse> getMeasurements(BasicTest test, Locale locale, CapabilitiesRequest capabilitiesRequest) {
        List<MobileMeasurementResultMeasurementResponse> measurementResponses = new ArrayList<>();
        addDownloadTestResultMeasurementResponse(measurementResponses, test, locale, capabilitiesRequest);
        addUploadTestResultMeasurementResponse(measurementResponses, test, locale, capabilitiesRequest);
        addPingTestResultMeasurementResponse(measurementResponses, test, locale, capabilitiesRequest);
        addSignalTestResultMeasurementResponse(measurementResponses, test, locale, capabilitiesRequest);
        return measurementResponses;
    }

    private void addSignalTestResultMeasurementResponse(List<MobileMeasurementResultMeasurementResponse> measurementResponses, BasicTest test, Locale locale, CapabilitiesRequest capabilitiesRequest) {
        boolean dualSim = MeasurementUtils.isDualSim(NetworkType.valueOfSafely(test.getNetworkType()).getValue(), test.getDualSim());
        boolean useSignal = MeasurementUtils.isUseSignal(test.getSimCount(), dualSim);

        if (Objects.nonNull(test.getSignal()) || Objects.nonNull(test.getLte_rsrp())) {
            MobileMeasurementResultMeasurementResponse.MobileMeasurementResultMeasurementResponseBuilder signalResponseBuilder = MobileMeasurementResultMeasurementResponse.builder()
                    .value(getSignalString(test, locale, useSignal));
            if (Objects.nonNull(test.getSignal())) {
                int[] threshold = ClassificationUtils.getThresholdForSignal(NetworkType.valueOfSafely(test.getNetworkType()).getValue());
                signalResponseBuilder
                        .classification(ClassificationUtils.classify(threshold, test.getSignal(), capabilitiesRequest.getClassification().getCount()))
                        .title(getStringFromBundle("RESULT_SIGNAL", locale));
            } else {
                signalResponseBuilder
                        .classification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_SIGNAL_RSRP, test.getLte_rsrp(), capabilitiesRequest.getClassification().getCount()))
                        .title(getStringFromBundle("RESULT_SIGNAL_RSRP", locale));
            }

            MobileMeasurementResultMeasurementResponse signalResponse = signalResponseBuilder.build();
            measurementResponses.add(signalResponse);
        }
    }

    private void addPingTestResultMeasurementResponse(List<MobileMeasurementResultMeasurementResponse> measurementResponses, BasicTest test, Locale locale, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getPing())
                .ifPresent(pingMedian -> {
                    MobileMeasurementResultMeasurementResponse pingResponse = MobileMeasurementResultMeasurementResponse.builder()
                            .title(getStringFromBundle("RESULT_PING", locale))
                            .value(getPingString(test, locale))
                            .classification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_PING, (long) (pingMedian * com.specure.common.constant.Constants.PING_CONVERSION_MULTIPLICATOR), capabilitiesRequest.getClassification().getCount()))
                            .build();
                    measurementResponses.add(pingResponse);
                });
    }

    private void addUploadTestResultMeasurementResponse(List<MobileMeasurementResultMeasurementResponse> measurementResponses, BasicTest test, Locale locale, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getUpload())
                .ifPresent(uploadSpeed -> {
                    MobileMeasurementResultMeasurementResponse uploadResponse = MobileMeasurementResultMeasurementResponse.builder()
                            .title(getStringFromBundle("RESULT_UPLOAD", locale))
                            .value(getUploadString(test, locale))
                            .classification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_UPLOAD, uploadSpeed, capabilitiesRequest.getClassification().getCount()))
                            .build();
                    measurementResponses.add(uploadResponse);
                });
    }

    private void addDownloadTestResultMeasurementResponse(List<MobileMeasurementResultMeasurementResponse> measurementResponses, BasicTest test, Locale locale, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getDownload())
                .ifPresent(downloadSpeed -> {
                    MobileMeasurementResultMeasurementResponse downloadResponse = MobileMeasurementResultMeasurementResponse.builder()
                            .title(getStringFromBundle("RESULT_DOWNLOAD", locale))
                            .value(getDownloadString(test, locale))
                            .classification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_DOWNLOAD, downloadSpeed, capabilitiesRequest.getClassification().getCount()))
                            .build();
                    measurementResponses.add(downloadResponse);
                });
    }

    private String getShareTextField4(BasicTest test, Locale locale, String signalString) {
        if (Objects.nonNull(signalString)) {
            if (Objects.isNull(test.getLte_rsrp())) {
                return MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT_SIGNAL_ADD", locale), signalString);
            } else {
                return MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT_RSRP_ADD", locale), signalString);
            }
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String getShareText(BasicTest test, String timeString, Locale locale) {
        boolean dualSim = MeasurementUtils.isDualSim(NetworkType.valueOfSafely(test.getNetworkType()).getValue(), test.getDualSim());
        boolean useSignal = MeasurementUtils.isUseSignal(test.getSimCount(), dualSim);
        String signalString = getSignalString(test, locale, useSignal);
        String shareLocationString = getShareLocationString(test, locale);
        String downloadString = getDownloadString(test, locale);
        String uploadString = getUploadString(test, locale);
        String pingString = getPingString(test, locale);
        String shareTextField4 = getShareTextField4(test, locale, signalString);
        String platformString = getPlatformString(test);
        String modelString = getModelString(test);
        String networkTypeString = getNetworkTypeString(test);
        String providerString = getProviderString(test, locale);
        String mobileNetworkString = getMobileNetworkString(test, locale);
        String urlShareString = getUrlShareString(test, locale);

        if (dualSim) {
            return MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT", locale),
                    timeString,
                    downloadString,
                    uploadString,
                    pingString,
                    shareTextField4,
                    getStringFromBundle("RESULT_DUAL_SIM", locale),
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    platformString,
                    modelString,
                    shareLocationString,
                    urlShareString);
        } else {
            return MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT", locale),
                    timeString,
                    downloadString,
                    uploadString,
                    pingString,
                    shareTextField4,
                    networkTypeString,
                    providerString,
                    mobileNetworkString,
                    platformString,
                    modelString,
                    shareLocationString,
                    urlShareString);
        }
    }

    private String getUrlShareString(BasicTest test, Locale locale) {
        return settingsService.getSettingsMap()
                .entrySet()
                .stream()
                .filter(x -> AdminSetting.URL_SHARE_KEY.equals(x.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(x -> String.join(StringUtils.EMPTY, x, test.getOpenTestUuid()))
                .orElse(String.join(StringUtils.EMPTY, "https://nettest.com/en/test/results/", test.getOpenTestUuid()));
    }

    private String getProviderString(BasicTest test, Locale locale) {
        return Optional.ofNullable(test.getOperator())
                .map(provider -> MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT_PROVIDER_ADD", locale),
                        provider))
                .orElse(StringUtils.EMPTY);
    }

    private String getNetworkTypeString(BasicTest test) {
        return Optional.ofNullable(test.getNetworkType())
                .map(NetworkType::valueOfSafely)
                .map(NetworkType::getValue)
                .map(HelperFunctions::getNetworkTypeName)
                .orElse(StringUtils.EMPTY);
    }

    private String getModelString(BasicTest test) {
        return Optional.ofNullable(test.getDevice())
                .orElse(StringUtils.EMPTY);
    }

    private String getPlatformString(BasicTest test) {
        return Optional.ofNullable(test.getPlatform())
                .orElse(StringUtils.EMPTY);
    }

    private String getShareLocationString(BasicTest test, Locale locale) {
//        return Optional.ofNullable(test.getTestLocation())
//                .map(testLocation -> MessageFormat.format(MessageBundleConstant.RESULT_SHARE_TEXT_LOCATION_ADD, getGeoLocationString(testLocation, locale)))
//                .orElse(StringUtils.EMPTY);
        return StringUtils.EMPTY;
    }

    private String getSignalString(BasicTest test, Locale locale, boolean useSignal) {
        if (useSignal) {
            if (Objects.nonNull(test.getSignal())) {
                return FormatUtils.formatValueAndUnit(test.getSignal(), getStringFromBundle("RESULT_SIGNAL_UNIT", locale));
            }
            if (Objects.nonNull(test.getLte_rsrp())) {
                return FormatUtils.formatValueAndUnit(test.getLte_rsrp(), getStringFromBundle("RESULT_SIGNAL_UNIT", locale));
            }
        }
        return null;
    }

    private String getPingString(BasicTest test, Locale locale) {
        return Optional.ofNullable(test.getPing())
                .map(pingMs -> FormatUtils.formatValueAndUnit(Double.valueOf(pingMs), getStringFromBundle("RESULT_PING_UNIT", locale), locale))
                .orElse(StringUtils.EMPTY);
    }

    private String getUploadString(BasicTest test, Locale locale) {
        return Optional.ofNullable(test.getUpload())
                .map(x -> x / com.specure.common.constant.Constants.BYTES_UNIT_CONVERSION_MULTIPLICATOR)
                .map(downloadSpeed -> FormatUtils.formatValueAndUnit(downloadSpeed, getStringFromBundle("RESULT_UPLOAD_UNIT", locale), locale))
                .orElse(StringUtils.EMPTY);
    }

    private String getDownloadString(BasicTest test, Locale locale) {
        return Optional.ofNullable(test.getDownload())
                .map(x -> x / com.specure.common.constant.Constants.BYTES_UNIT_CONVERSION_MULTIPLICATOR)
                .map(downloadSpeed -> FormatUtils.formatValueAndUnit(downloadSpeed, getStringFromBundle("RESULT_DOWNLOAD_UNIT", locale), locale))
                .orElse(StringUtils.EMPTY);
    }

    private String getMobileNetworkString(BasicTest test, Locale locale) {
        if (Objects.nonNull(test.getOperator())) {
            return MessageFormat.format(getStringFromBundle("RESULT_SHARE_TEXT_MOBILE_ADD", locale), test.getOperator());
        } else {
            return StringUtils.EMPTY;
        }
    }

    private void setNetFields(MobileMeasurementResultResponse.MobileMeasurementResultResponseBuilder testResultResponseBuilder, BasicTest test, Locale locale) {
        List<NetItemResponse> netItemResponses = new ArrayList<>();
        NetworkInfoResponse.NetworkInfoResponseBuilder networkInfoResponseBuilder = NetworkInfoResponse.builder();
        boolean dualSim = MeasurementUtils.isDualSim(NetworkType.valueOfSafely(test.getNetworkType()).getValue(), test.getDualSim());
        boolean useSignal = MeasurementUtils.isUseSignal(test.getSimCount(), dualSim);
        if (useSignal) {
            String networkTypeName = HelperFunctions.getNetworkTypeName(NetworkType.valueOfSafely(test.getNetworkType()).getValue());
            addNetItemResponse(locale, netItemResponses, networkTypeName, "RESULT_NETWORK_TYPE");
            networkInfoResponseBuilder.networkTypeLabel(networkTypeName);
        } else {
            addNetItemResponse(locale, netItemResponses, getStringFromBundle("RESULT_DUAL_SIM", locale), "RESULT_NETWORK_TYPE");
            networkInfoResponseBuilder.networkTypeLabel(getStringFromBundle("RESULT_DUAL_SIM", locale));
        }
        int networkType = ObjectUtils.defaultIfNull(NetworkType.valueOfSafely(test.getNetworkType()).getValue(), 0);
        if (networkType == 98 || networkType == 99) // mobile wifi or browser
        {
            if (networkType == 99)  // mobile wifi
            {
                Optional.ofNullable(test.getWifiSsid())
                        .map(fieldAnonymizerFilter::getWifiSsidFilter)
                        .ifPresent(wifiSSID -> {
                            addNetItemResponse(locale, netItemResponses, wifiSSID, "RESULT_WIFI_SSID");
                            networkInfoResponseBuilder.wifiSSID(wifiSSID);
                        });
            }
        } else {
            if (!dualSim) {
                Optional.ofNullable(test.getOperator())
                        .ifPresent(networkOperatorName -> {
                            addNetItemResponse(locale, netItemResponses, networkOperatorName, "RESULT_OPERATOR_NAME");
                            networkInfoResponseBuilder.providerName(networkOperatorName);
                        });
            }
        }
        testResultResponseBuilder.netItemResponses(netItemResponses);
        testResultResponseBuilder.networkInfoResponse(networkInfoResponseBuilder.build());
        testResultResponseBuilder.networkType(networkType);
    }

    private MeasurementResultResponse getMeasurementResult(BasicTest test, CapabilitiesRequest capabilitiesRequest) {
        MeasurementResultResponse.MeasurementResultResponseBuilder measurementResultResponseBuilder = MeasurementResultResponse.builder();
        setDownloadFields(test, measurementResultResponseBuilder, capabilitiesRequest);
        setUploadFields(test, measurementResultResponseBuilder, capabilitiesRequest);
        setPingFields(test, measurementResultResponseBuilder, capabilitiesRequest);
        setSignalFields(test, measurementResultResponseBuilder, capabilitiesRequest);
        return measurementResultResponseBuilder
                .jitter(String.valueOf(test.getJitter()))
                .packetLoss(String.valueOf(test.getPacketLoss()))
                .build();
    }

    private void addNetItemResponse(Locale locale, List<NetItemResponse> netItemResponses, String providerName, String titleKey) {
        NetItemResponse netItemResponse = NetItemResponse.builder()
                .title(getStringFromBundle(titleKey, locale))
                .value(providerName)
                .build();
        netItemResponses.add(netItemResponse);
    }

    private void setDownloadFields(BasicTest test, MeasurementResultResponse.MeasurementResultResponseBuilder measurementResultResponseBuilder, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getDownload())
                .ifPresent(downloadSpeed -> measurementResultResponseBuilder
                        .downloadClassification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_DOWNLOAD, downloadSpeed, capabilitiesRequest.getClassification().getCount()))
                        .downloadKBit(downloadSpeed));
    }

    private void setSignalFields(BasicTest test, MeasurementResultResponse.MeasurementResultResponseBuilder measurementResultResponseBuilder, CapabilitiesRequest capabilitiesRequest) {

        if (Objects.nonNull(test.getSignal())) {
            int[] threshold = ClassificationUtils.getThresholdForSignal(NetworkType.valueOfSafely(test.getNetworkType()).getValue());
            measurementResultResponseBuilder
                    .signalStrength(test.getSignal())
                    .signalClassification(ClassificationUtils.classify(threshold, test.getSignal(), capabilitiesRequest.getClassification().getCount()));
        }
        if (Objects.nonNull(test.getLte_rsrp())) {
            measurementResultResponseBuilder
                    .lteRSRP(test.getLte_rsrp())
                    .signalClassification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_SIGNAL_RSRP, test.getLte_rsrp(), capabilitiesRequest.getClassification().getCount()));
        }
    }

    private void setPingFields(BasicTest test, MeasurementResultResponse.MeasurementResultResponseBuilder measurementResultResponseBuilder, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getPing())
                .ifPresent(pingMedian -> measurementResultResponseBuilder
                        .pingClassification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_PING, (long) (pingMedian * com.specure.common.constant.Constants.PING_CONVERSION_MULTIPLICATOR), capabilitiesRequest.getClassification().getCount()))
                        .pingMs(pingMedian));
    }

    private void setUploadFields(BasicTest test, MeasurementResultResponse.MeasurementResultResponseBuilder measurementResultResponseBuilder, CapabilitiesRequest capabilitiesRequest) {
        Optional.ofNullable(test.getUpload())
                .ifPresent(uploadSpeed -> measurementResultResponseBuilder
                        .uploadClassification(ClassificationUtils.classify(ClassificationUtils.THRESHOLD_UPLOAD, uploadSpeed, capabilitiesRequest.getClassification().getCount()))
                        .uploadKBit(uploadSpeed));
    }

    private double getPingMsFromPingMedian(Long x) {
        return x / com.specure.common.constant.Constants.PING_CONVERSION_MULTIPLICATOR;
    }

}
