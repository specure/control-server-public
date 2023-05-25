package com.specure.service.mobile.impl;

import com.specure.common.constant.AdminSetting;
import com.specure.common.enums.NetworkType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementDetails;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.common.service.digger.DiggerService;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Traits;
import com.specure.config.ApplicationProperties;
import com.specure.constant.ErrorMessage;
import com.specure.dto.mobile.QoeClassificationThresholds;
import com.specure.mapper.core.MeasurementMapper;
import com.specure.mapper.mobile.MobileMeasurementMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.CapabilitiesRequest;
import com.specure.request.core.ClassificationRequest;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementResultDetailRequest;
import com.specure.request.mobile.MobileMeasurementResultRequest;
import com.specure.response.mobile.MobileMeasurementResultDetailContainerResponse;
import com.specure.response.mobile.MobileMeasurementResultDetailResponse;
import com.specure.response.mobile.MobileMeasurementResultMeasurementResponse;
import com.specure.response.mobile.NetItemResponse;
import com.specure.sah.TestConstants;
import com.specure.service.admin.RawProviderService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.SettingsService;
import com.specure.service.mobile.MobileMeasurementService;
import com.specure.service.sah.BasicTestService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.specure.core.TestConstants.DEFAULT_ASN_LONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MobileMeasurementServiceImplTest {
    private final static int DEFAULT_TEST_FIELDS_COUNT = 11;
    private final static int TIME_TEST_FIELDS_COUNT = 5;

    private MobileMeasurementService mobileMeasurementService;

    private final static ApplicationProperties applicationProperties = new ApplicationProperties(
            new ApplicationProperties.LanguageProperties(Set.of("en", "de"), "en"),
            Set.of("RMBT", "RMBTjs", "Open-RMBT", "RMBTws", "HW-PROBE"),
            "0.1.0 || 0.3.0 || ^1.0.0",
            1,
            2,
            3,
            10000,
            2000
    );
    private final Map<String, String> headers = new HashMap<>();


    @Mock
    private MessageSource messageSource;
    @Mock
    private MeasurementServerRepository measurementServerRepository;
    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private MobileMeasurementMapper mobileMeasurementMapper;
    @Mock
    private BasicTestService sahBasicTestService;
    @Mock
    private MultiTenantManager multiTenantManager;
    @Mock
    private RawProviderService providerService;
    @Mock
    private DiggerService diggerService;
    @MockBean
    private MeasurementMapper measurementMapper;
    @Mock
    private SettingsService settingsService;
    @Mock
    private FieldAnonymizerFilter fieldAnonymizerFilter;

    @Mock
    private Measurement measurement;
    @Mock
    private BasicTest basicTest;
    @Mock
    private MobileMeasurementResultDetailRequest testResultDetailRequest;
    @Mock
    private MobileMeasurementResultRequest testResultRequest;
    @Mock
    private CapabilitiesRequest capabilitiesRequest;
    @Mock
    private ClassificationRequest classificationRequest;
    @Mock
    private QoeClassificationThresholds qoeClassificationThresholds;
    @Mock
    private MeasurementResultMobileRequest measurementResultMobileRequest;
    @Mock
    private Measurement updateMeasurement;
    @Mock
    private MeasurementServer measurementServer;
    @Mock
    private CityResponse cityResponse;
    @Mock
    private Traits traits;
    @Mock
    private RawProvider provider;
    @Mock
    private MeasurementDetails measurementDetails;


    @Before
    public void setUp() {
        headers.put("x-real-ip", TestConstants.DEFAULT_IP);
        Locale.setDefault(Locale.ENGLISH);
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:SystemMessages");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        messageSource = reloadableResourceBundleMessageSource;
        mobileMeasurementService = new MobileMeasurementServiceImpl(applicationProperties,
                messageSource,
                measurementServerRepository,
                measurementRepository,
                mobileMeasurementMapper,
                sahBasicTestService,
                multiTenantManager,
                providerService,
                diggerService,
                measurementMapper,
                settingsService,
                fieldAnonymizerFilter);
    }

    @Test
    public void processMeasurementResult_whenNetworkMccMncIsNull_expectSaveMeasurementToElasticAndSaveCalled() {
        when(measurementResultMobileRequest.getTestToken()).thenReturn(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN);
        when(measurementRepository.findByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(Optional.of(measurement));
        when(mobileMeasurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement))
                .thenReturn(updateMeasurement);
        when(diggerService.getCityResponseByIpAddress(TestConstants.DEFAULT_IP)).thenReturn(Optional.of(cityResponse));
        when(cityResponse.getTraits()).thenReturn(traits);
        when(traits.getIsp()).thenReturn(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME);
        when(traits.getMobileCountryCode()).thenReturn("332");
        when(traits.getMobileNetworkCode()).thenReturn("33");
        when(traits.getAutonomousSystemNumber()).thenReturn(null);
        when(diggerService.digASN(TestConstants.DEFAULT_IP)).thenReturn(DEFAULT_ASN_LONG);
        when(providerService.getRawProvider(cityResponse, DEFAULT_ASN_LONG, null)).thenReturn(provider);
        when(measurement.getMeasurementDetails()).thenReturn(measurementDetails);

        mobileMeasurementService.processMeasurementResult(measurementResultMobileRequest, headers);

        verify(measurement).setNetworkMccMnc("332-33");
        verify(sahBasicTestService).saveMeasurementMobileToElastic(updateMeasurement);
        verify(measurementMapper).updateMeasurementProviderInfo(measurement, provider);
        verify(measurement).setNetworkOperator(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME);
    }

    @Test
    public void processMeasurementResult_whenCommonData_expectSaveMeasurementToElasticAndSaveCalled() {
        when(measurementResultMobileRequest.getTestToken()).thenReturn(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN);
        when(measurementResultMobileRequest.getNetworkMccMnc()).thenReturn(TestConstants.DEFAULT_NETWORK_MCC_MNC);
        when(measurementRepository.findByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(Optional.of(measurement));
        when(mobileMeasurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement))
                .thenReturn(updateMeasurement);
        when(diggerService.getCityResponseByIpAddress(TestConstants.DEFAULT_IP)).thenReturn(Optional.of(cityResponse));
        when(cityResponse.getTraits()).thenReturn(traits);
        when(traits.getIsp()).thenReturn(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME);
        when(traits.getMobileCountryCode()).thenReturn("332");
        when(traits.getMobileNetworkCode()).thenReturn("33");
        when(traits.getAutonomousSystemNumber()).thenReturn(null);
        when(diggerService.digASN(TestConstants.DEFAULT_IP)).thenReturn(DEFAULT_ASN_LONG);
        when(providerService.getRawProvider(cityResponse, DEFAULT_ASN_LONG, TestConstants.DEFAULT_NETWORK_MCC_MNC)).thenReturn(provider);
        when(measurement.getMeasurementDetails()).thenReturn(measurementDetails);

        mobileMeasurementService.processMeasurementResult(measurementResultMobileRequest, headers);

        verify(sahBasicTestService).saveMeasurementMobileToElastic(updateMeasurement);
        verify(measurementMapper).updateMeasurementProviderInfo(measurement, provider);
        verify(measurement).setNetworkOperator(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME);
        verify(fieldAnonymizerFilter).refreshAnonymizedIpAddress(TestConstants.DEFAULT_IP, TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
    }

    @Test
    public void processMeasurementResult_whenCityResponseIsEmpty_expectSaveMeasurementToElasticAndSaveCalled() {
        when(measurementResultMobileRequest.getTestToken()).thenReturn(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN);
        when(measurementResultMobileRequest.getNetworkMccMnc()).thenReturn(TestConstants.DEFAULT_MCC_MNC);
        when(measurementRepository.findByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(Optional.of(measurement));
        when(mobileMeasurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement))
                .thenReturn(updateMeasurement);
        when(diggerService.getCityResponseByIpAddress(TestConstants.DEFAULT_IP)).thenReturn(Optional.empty());
        when(diggerService.digASN(TestConstants.DEFAULT_IP)).thenReturn(DEFAULT_ASN_LONG);
        when(measurement.getMeasurementDetails()).thenReturn(measurementDetails);

        mobileMeasurementService.processMeasurementResult(measurementResultMobileRequest, headers);

        verify(sahBasicTestService).saveMeasurementMobileToElastic(updateMeasurement);
        verify(measurement).setNetworkMccMnc(TestConstants.DEFAULT_MCC_MNC);
        verify(measurement).setNetworkOperator(ErrorMessage.UNKNOWN_PROVIDER);
    }

    @Test(expected = MeasurementNotFoundByUuidException.class)
    public void processMeasurementResult_whenCommonData_expectException() {
        when(measurementResultMobileRequest.getTestToken()).thenReturn(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN);

        mobileMeasurementService.processMeasurementResult(measurementResultMobileRequest, headers);

        verifyNoInteractions(measurementRepository);
        verifyNoInteractions(sahBasicTestService);
    }


    @Test
    @Ignore
    public void getTestResultDetailByTestUUID_whenTestExistAndTime_expectMobileMeasurementResultDetailResponse() {
        BasicTest test = getTimeTest();
        when(testResultDetailRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultDetailRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID))
                .thenReturn(test);
        when(measurementServerRepository.findById(anyLong())).thenReturn(Optional.of(measurementServer));
        when(measurementServer.getName()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME);

        var result = mobileMeasurementService.getTestResultDetailByTestUUID(testResultDetailRequest);

        assertEquals(TIME_TEST_FIELDS_COUNT, result.getMobileMeasurementResultDetailContainerResponse().size());
        for (int i = 0; i < TIME_TEST_FIELDS_COUNT; i++) {
            assertEquals(getTimeMobileMeasurementResultDetailResponse().getMobileMeasurementResultDetailContainerResponse().get(i), result.getMobileMeasurementResultDetailContainerResponse().get(i));
        }
        assertEquals(getTimeMobileMeasurementResultDetailResponse(), result);
    }

    @Test
    public void getTestResultDetailByTestUUID_whenTestExistAndDualSimFalse_expectMobileMeasurementResultDetailResponse() {
        BasicTest test = getDualSimTest();
        when(testResultDetailRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultDetailRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING));
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING))
                .thenReturn(test);
        when(measurementServerRepository.findById(anyLong())).thenReturn(Optional.of(measurementServer));

        var result = mobileMeasurementService.getTestResultDetailByTestUUID(testResultDetailRequest);

        assertEquals(getDualSimResult(), result);
    }

    @Test
    public void getTestResultDetailByTestUUID_whenTestExist_expectMobileMeasurementResultDetailResponse() {
        BasicTest test = getDefaultTest();
        when(testResultDetailRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultDetailRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING));
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING))
                .thenReturn(test);
        when(measurementServerRepository.findById(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(Optional.of(getMeasurementServer()));
        when(fieldAnonymizerFilter.getWifiSsidFilter(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(TestConstants.DEFAULT_WIFI_SSID);

        var result = mobileMeasurementService.getTestResultDetailByTestUUID(testResultDetailRequest);

        assertEquals(DEFAULT_TEST_FIELDS_COUNT, result.getMobileMeasurementResultDetailContainerResponse().size());
        for (int i = 0; i < DEFAULT_TEST_FIELDS_COUNT; i++) {
            assertEquals(getDefaultTestResultResponse().getMobileMeasurementResultDetailContainerResponse().get(i), result.getMobileMeasurementResultDetailContainerResponse().get(i));
        }
        assertEquals(getDefaultTestResultResponse(), result);
    }

    @Test
    public void getTestResult_whenSignalStrengthNotNullAndUseSignalTrueAndDualSimTrue_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getDualSim()).thenReturn(Boolean.TRUE);
        when(basicTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
        when(basicTest.getSimCount()).thenReturn(TestConstants.DEFAULT_TEST_SIM_COUNT);
        when(basicTest.getDownload()).thenReturn(TestConstants.DEFAULT_RESULT_DOWNLOAD_SPEED);
        when(basicTest.getUpload()).thenReturn(TestConstants.DEFAULT_RESULT_UPLOAD_SPEED);
        when(basicTest.getPing()).thenReturn(TestConstants.DEFAULT_PING);
        when(basicTest.getSignal()).thenReturn(TestConstants.DEFAULT_SIGNAL_STRENGTH_FIRST);
        when(basicTest.getLte_rsrp()).thenReturn(null);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);

        var result = mobileMeasurementService.getTestResult(testResultRequest);
        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID, testResultResponse.getOpenTestUUID());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_STRING, testResultResponse.getTimeString());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_SUBJECT, testResultResponse.getShareSubject());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_DUAL_SIM_TRUE_SIGNAL_STRENGTH_NOT_NULL, testResultResponse.getShareText());
        assertEquals(getMeasurementIfSignalStrengthNotNull(), testResultResponse.getMeasurement());
    }

    @Test
    public void getTestResult_whenSettingContainsShareUrl_expectTestResultContainerResponse() {
        when(settingsService.getSettingsMap()).thenReturn(Map.of(AdminSetting.URL_SHARE_KEY, TestConstants.DEFAULT_TEXT));
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getDualSim()).thenReturn(Boolean.TRUE);
        when(basicTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
        when(basicTest.getSimCount()).thenReturn(TestConstants.DEFAULT_TEST_SIM_COUNT);
        when(basicTest.getDownload()).thenReturn(TestConstants.DEFAULT_RESULT_DOWNLOAD_SPEED);
        when(basicTest.getUpload()).thenReturn(TestConstants.DEFAULT_RESULT_UPLOAD_SPEED);
        when(basicTest.getPing()).thenReturn(TestConstants.DEFAULT_PING);
        when(basicTest.getSignal()).thenReturn(TestConstants.DEFAULT_SIGNAL_STRENGTH_FIRST);
        when(basicTest.getLte_rsrp()).thenReturn(null);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);

        var result = mobileMeasurementService.getTestResult(testResultRequest);
        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID, testResultResponse.getOpenTestUUID());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_STRING, testResultResponse.getTimeString());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_SUBJECT, testResultResponse.getShareSubject());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_SHARE_URL, testResultResponse.getShareText());
        assertEquals(getMeasurementIfSignalStrengthNotNull(), testResultResponse.getMeasurement());
    }

    @Test
    public void getTestResult_whenLteRSRPNotNullAndUseSignalTrueAndDualSimFalse_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getDualSim()).thenReturn(Boolean.FALSE);
        when(basicTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
        when(basicTest.getSimCount()).thenReturn(TestConstants.DEFAULT_TEST_SIM_COUNT);
        when(basicTest.getDownload()).thenReturn(TestConstants.DEFAULT_RESULT_DOWNLOAD_SPEED);
        when(basicTest.getUpload()).thenReturn(TestConstants.DEFAULT_RESULT_UPLOAD_SPEED);
        when(basicTest.getPing()).thenReturn(TestConstants.DEFAULT_PING);
        when(basicTest.getSignal()).thenReturn(null);
        when(basicTest.getLte_rsrp()).thenReturn(TestConstants.DEFAULT_LTE_RSRP_FIRST);
        when(basicTest.getOperator()).thenReturn(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);

        var result = mobileMeasurementService.getTestResult(testResultRequest);
        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID, testResultResponse.getOpenTestUUID());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_STRING, testResultResponse.getTimeString());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_SUBJECT, testResultResponse.getShareSubject());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_RESPONSE_SHARE_TEXT_DUAL_SIM_FALSE_LTE_RSRP_NOT_NULL, testResultResponse.getShareText());
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(getMeasurementIfLteRSRPNotNull(), testResultResponse.getMeasurement());
    }

    @Test
    public void getTestResult_whenDualSimFalseAndBrowser_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getNetworkType()).thenReturn(NetworkType.WLAN.name());
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getDualSim()).thenReturn(Boolean.TRUE);
        when(basicTest.getSimCount()).thenReturn(null);
        when(basicTest.getWifiSsid()).thenReturn(TestConstants.DEFAULT_WIFI_SSID);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);
        when(fieldAnonymizerFilter.getWifiSsidFilter(TestConstants.DEFAULT_WIFI_SSID)).thenReturn(TestConstants.DEFAULT_WIFI_SSID);

        var result = mobileMeasurementService.getTestResult(testResultRequest);

        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(NetworkType.WLAN.name(), testResultResponse.getNetworkInfoResponse().getNetworkTypeLabel());
        assertEquals(TestConstants.DEFAULT_WIFI_SSID, testResultResponse.getNetworkInfoResponse().getWifiSSID());
        assertEquals(2, testResultResponse.getNetItemResponses().size());
        assertEquals(getNetItemResponses(NetworkType.WLAN.name(), TestConstants.DEFAULT_NET_ITEM_RESPONSE_NETWORK_TYPE_TITLE), testResultResponse.getNetItemResponses().get(0));
        assertEquals(getNetItemResponses(TestConstants.DEFAULT_WIFI_SSID, TestConstants.DEFAULT_NET_ITEM_RESPONSE_WIFI_SSID_TITLE), testResultResponse.getNetItemResponses().get(1));


    }

    @Test
    public void getTestResult_whenDualSimFalseAndNotBrowserAndNotWLAN_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE);
        when(basicTest.getDualSim()).thenReturn(Boolean.FALSE);
        when(basicTest.getOperator()).thenReturn(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);

        var result = mobileMeasurementService.getTestResult(testResultRequest);

        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME, testResultResponse.getNetworkInfoResponse().getProviderName());
        assertEquals(2, testResultResponse.getNetItemResponses().size());
        assertEquals(getNetItemResponses(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME, TestConstants.DEFAULT_NET_ITEM_RESPONSE_OPERATOR_NAME_TITLE), testResultResponse.getNetItemResponses().get(1));

    }

    @Test
    public void getTestResult_whenQoeClassificaitonNotNull_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getPing()).thenReturn(TestConstants.DEFAULT_PING);
        when(basicTest.getDownload()).thenReturn(TestConstants.DEFAULT_RESULT_DOWNLOAD_SPEED);
        when(basicTest.getUpload()).thenReturn(TestConstants.DEFAULT_RESULT_UPLOAD_SPEED);
        when(basicTest.getJitter()).thenReturn(TestConstants.DEFAULT_JITTER);
        when(basicTest.getPacketLoss()).thenReturn(TestConstants.DEFAULT_PACKET_LOSS);
        when(qoeClassificationThresholds.getQoeCategory()).thenReturn(TestConstants.DEFAULT_QOE_CATEGORY);

        var result = mobileMeasurementService.getTestResult(testResultRequest);

        assertEquals(1, result.getMobileMeasurementResultRespons().size());
        var testResultResponse = result.getMobileMeasurementResultRespons().get(0);
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), testResultResponse.getMeasurementResult().getJitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), testResultResponse.getMeasurementResult().getPacketLoss());
        assertEquals(14, testResultResponse.getQoeClassificationResponses().size());
        var qoeClassificationResponse = testResultResponse.getQoeClassificationResponses().get(0);
        assertEquals(TestConstants.DEFAULT_QOE_CATEGORY.getValue(), qoeClassificationResponse.getCategory());
        assertEquals(TestConstants.DEFAULT_QOE_CLASSIFICATION, qoeClassificationResponse.getClassification());
        assertEquals(TestConstants.DEFAULT_QUALITY, qoeClassificationResponse.getQuality());
    }

    @Test
    public void getTestResult_whenDEFAULT_expectTestResultContainerResponse() {
        when(testResultRequest.getLanguage()).thenReturn(TestConstants.DEFAULT_LANGUAGE);
        when(testResultRequest.getTestUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(testResultRequest.getCapabilitiesRequest()).thenReturn(capabilitiesRequest);
        when(capabilitiesRequest.getClassification()).thenReturn(classificationRequest);
        when(classificationRequest.getCount()).thenReturn(TestConstants.DEFAULT_CLASSIFICATION_COUNT);
        when(sahBasicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getTimestamp()).thenReturn(TestConstants.DEFAULT_TIMESTAMP);
        when(basicTest.getMeasurementDate()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_DATE);
        when(basicTest.getNetworkType()).thenReturn(TestConstants.DEFAULT_NETWORK_TYPE_LTE.name());

        var result = mobileMeasurementService.getTestResult(testResultRequest);

        assertEquals(1, result.getMobileMeasurementResultRespons().size());
    }


    private NetItemResponse getNetItemResponses(String value, String title) {
        return NetItemResponse.builder()
                .title(title)
                .value(value)
                .build();
    }

    private List<MobileMeasurementResultMeasurementResponse> getMeasurementIfSignalStrengthNotNull() {
        var speedDownloadMeasurementResponse = getSpeedDownloadMeasurementResponse();
        var speedUploadMeasurementResponse = getSpeedUploadMeasurementResponse();
        var pingMeasurementResponse = getPingMeasurementResponse();
        var signalMeasurementResponse = getSignalStrengthMeasurementResponse();
        return new ArrayList<>(List.of(
                speedDownloadMeasurementResponse,
                speedUploadMeasurementResponse,
                pingMeasurementResponse,
                signalMeasurementResponse
        ));
    }

    private List<MobileMeasurementResultMeasurementResponse> getMeasurementIfLteRSRPNotNull() {
        var speedDownloadMeasurementResponse = getSpeedDownloadMeasurementResponse();
        var speedUploadMeasurementResponse = getSpeedUploadMeasurementResponse();
        var pingMeasurementResponse = getPingMeasurementResponse();
        var signalMeasurementResponse = getSignalLteRSRPMeasurementResponse();
        return new ArrayList<>(List.of(
                speedDownloadMeasurementResponse,
                speedUploadMeasurementResponse,
                pingMeasurementResponse,
                signalMeasurementResponse
        ));
    }

    private MobileMeasurementResultMeasurementResponse getSignalLteRSRPMeasurementResponse() {
        return MobileMeasurementResultMeasurementResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_SIGNAL_RSRP_VALUE)
                .title(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_LTE_RSRP_TITLE)
                .classification(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_CLASSIFICATION)
                .build();
    }

    private MobileMeasurementResultMeasurementResponse getSignalStrengthMeasurementResponse() {
        return MobileMeasurementResultMeasurementResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_SIGNAL_STRENGTH_VALUE)
                .title(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_STRENGTH_TITLE)
                .classification(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_STRENGTH_CLASSIFICATION)
                .build();
    }

    private MobileMeasurementResultMeasurementResponse getPingMeasurementResponse() {
        return MobileMeasurementResultMeasurementResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_PING_MEDIAN_VALUE)
                .title(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_PING_TITLE)
                .classification(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_CLASSIFICATION)
                .build();
    }

    private MobileMeasurementResultMeasurementResponse getSpeedUploadMeasurementResponse() {
        return MobileMeasurementResultMeasurementResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_UPLOAD_VALUE)
                .title(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_SIGNAL_UPLOAD_TITLE)
                .classification(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_CLASSIFICATION)
                .build();
    }

    private MobileMeasurementResultMeasurementResponse getSpeedDownloadMeasurementResponse() {
        return MobileMeasurementResultMeasurementResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_DOWNLOAD_VALUE)
                .title(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_DOWNLOAD_TITLE)
                .classification(TestConstants.DEFAULT_TEST_RESULT_MEASUREMENT_RESPONSE_CLASSIFICATION)
                .build();
    }


    private BasicTest getTimeTest() {
        return getDefaultTestBuilder()
                .measurementDate(ZonedDateTime.ofInstant(TestConstants.DEFAULT_TIMESTAMP.toInstant(), ZoneId.of(TestConstants.DEFAULT_TIME_ZONE)).toString())
                .timestamp(TestConstants.DEFAULT_TIMESTAMP.toString())
                .build();
    }

    private MobileMeasurementResultDetailResponse getBandMobileMeasurementResultDetailResponse() {
        var properties = getDefaultProperties();
        properties.addAll(List.of(
                getMobileMeasurementResultDetailContainerResponse("key_frequency_dl", TestConstants.DEFAULT_TEST_RESULT_DETAIL_FREQUENCY_DL),
                getMobileMeasurementResultDetailContainerResponse("key_radio_band", TestConstants.DEFAULT_TEST_RESULT_DETAIL_RADIO_BAND)
        ));
        return MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(properties)
                .build();
    }

    private MobileMeasurementResultDetailResponse getNdtMobileMeasurementResultDetailResponse() {
        var properties = getDefaultProperties();
        properties.addAll(List.of(
                getMobileMeasurementResultDetailContainerResponse("key_speed_download_ndt", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_DOWNLOAD_NDT),
                getMobileMeasurementResultDetailContainerResponse("key_speed_upload_ndt", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_UPLOAD_NDT),
                getMobileMeasurementResultDetailContainerResponse("key_ndt_details_main", TestConstants.DEFAULT_TEST_NDT_MAIN),
                getMobileMeasurementResultDetailContainerResponse("key_ndt_details_stat", TestConstants.DEFAULT_TEST_NDT_STAT),
                getMobileMeasurementResultDetailContainerResponse("key_ndt_details_diag", TestConstants.DEFAULT_TEST_NDT_DIAG)
        ));
        return MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(properties)
                .build();
    }

    private BasicTest getDualSimTest() {
        return getDefaultTestBuilder()
                .dualSim(false)
                .measurementServerId(null)
                .signal(TestConstants.DEFAULT_SIGNAL_STRENGTH_FIRST)
                .lte_rsrp(TestConstants.DEFAULT_LTE_RSRP_FIRST)
                .lte_rsrq(TestConstants.DEFAULT_LTE_RSRQ_FIRST)
                .operator(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME)
                .build();
    }

    private BasicTest getDefaultTest() {
        return getDefaultTestBuilder()
                .measurementServerId(null)
                .download(TestConstants.DEFAULT_RESULT_DOWNLOAD_SPEED)
                .upload(TestConstants.DEFAULT_RESULT_UPLOAD_SPEED)
                .ping(TestConstants.DEFAULT_PING)
                .wifiSsid(TestConstants.DEFAULT_WIFI_SSID)
                .measurementServerId(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
                .model(TestConstants.DEFAULT_MODEL)
                .device(TestConstants.DEFAULT_DEVICE)
                .clientName(TestConstants.DEFAULT_TEST_SERVER_SERVER_TYPE.getName())
                .clientVersion(TestConstants.DEFAULT_CLIENT_VERSION)
                .build();
    }

    private MeasurementServer getMeasurementServer() {
        return MeasurementServer.builder()
                .name(TestConstants.DEFAULT_TEST_SERVER_NAME)
                .build();
    }

    private MobileMeasurementResultDetailResponse getDefaultTestResultResponse() {
        var properties = getDefaultProperties();
        properties.addAll(List.of(
                getMobileMeasurementResultDetailContainerResponse("key_network_type", TestConstants.DEFAULT_NETWORK_TYPE_LTE_RESULT_DETAIL_RESPONSE_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_speed_download", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_DOWNLOAD_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_speed_upload", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SPEED_UPLOAD_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_ping_median", TestConstants.DEFAULT_TEST_RESULT_DETAIL_PING_MEDIAN_VALUE),
//                getMobileMeasurementResultDetailContainerResponse("key_country_asn", TestConstants.DEFAULT_TEST_COUNTRY_ASN),
//                getMobileMeasurementResultDetailContainerResponse("key_country_geoip", TestConstants.DEFAULT_TEST_GEO_IP),
//                getMobileMeasurementResultDetailContainerResponse("key_client_public_ip", TestConstants.DEFAULT_TEST_CLIENT_PUBLIC_IP),
//                getMobileMeasurementResultDetailContainerResponse("key_client_public_ip_as", TestConstants.DEFAULT_TEST_PUBLIC_IP_ASN.toString()),
//                getMobileMeasurementResultDetailContainerResponse("key_client_public_ip_as_name", TestConstants.DEFAULT_TEST_PUBLIC_IP_AS_NAME),
//                getMobileMeasurementResultDetailContainerResponse("key_client_public_ip_rdns", TestConstants.DEFAULT_TEST_PUBLIC_IP_RDNS),
//                getMobileMeasurementResultDetailContainerResponse("key_provider", TestConstants.DEFAULT_TEST_PROVIDER_NAME),
//                getMobileMeasurementResultDetailContainerResponse("key_client_local_ip", TestConstants.DEFAULT_TEST_CLIENT_IP_LOCAL_TYPE),
//                getMobileMeasurementResultDetailContainerResponse("key_nat_type", TestConstants.DEFAULT_TEST_NAT_TYPE),
                getMobileMeasurementResultDetailContainerResponse("key_wifi_ssid", TestConstants.DEFAULT_WIFI_SSID),
//                getMobileMeasurementResultDetailContainerResponse("key_wifi_bssid", TestConstants.DEFAULT_WIFI_BSSID),
//                getMobileMeasurementResultDetailContainerResponse("key_wifi_link_speed", TestConstants.DEFAULT_TEST_RESULT_DETAIL_WIFI_LINK_SPEED),
//                getMobileMeasurementResultDetailContainerResponse("key_total_bytes", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TOTAL_BYTES),
//                getMobileMeasurementResultDetailContainerResponse("key_total_if_bytes", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TOTAL_BYTES_IF),
//                getMobileMeasurementResultDetailContainerResponse("key_testdl_if_bytes_download", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TEST_DL_IF_BYTES_DOWNLOAD),
//                getMobileMeasurementResultDetailContainerResponse("key_testdl_if_bytes_upload", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TEST_DL_IF_BYTES_UPLOAD),
//                getMobileMeasurementResultDetailContainerResponse("key_testul_if_bytes_download", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TEST_UL_IF_BYTES_DOWNLOAD),
//                getMobileMeasurementResultDetailContainerResponse("key_testul_if_bytes_upload", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TEST_UL_IF_BYTES_UPLOAD),
//                getMobileMeasurementResultDetailContainerResponse("key_time_dl", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_DL),
//                getMobileMeasurementResultDetailContainerResponse("key_time_ul", TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_UL),
//                getMobileMeasurementResultDetailContainerResponse("key_duration_dl", TestConstants.DEFAULT_TEST_RESULT_DETAIL_DURATION_DL),
//                getMobileMeasurementResultDetailContainerResponse("key_duration_ul", TestConstants.DEFAULT_TEST_RESULT_DETAIL_DURATION_UL),
                getMobileMeasurementResultDetailContainerResponse("key_server_name", TestConstants.DEFAULT_TEST_SERVER_NAME),
                getMobileMeasurementResultDetailContainerResponse("key_plattform", TestConstants.DEFAULT_TEST_PLATFORM.name()),
//                getMobileMeasurementResultDetailContainerResponse("key_os_version", TestConstants.DEFAULT_OS_VERSION),
                getMobileMeasurementResultDetailContainerResponse("key_model", TestConstants.DEFAULT_DEVICE),
                getMobileMeasurementResultDetailContainerResponse("key_client_name", TestConstants.DEFAULT_CLIENT_NAME),
//                getMobileMeasurementResultDetailContainerResponse("key_client_software_version", TestConstants.DEFAULT_CLIENT_SOFTWARE_VERSION),
                getMobileMeasurementResultDetailContainerResponse("key_client_version", TestConstants.DEFAULT_CLIENT_VERSION)
//                getMobileMeasurementResultDetailContainerResponse("key_duration", TestConstants.DEFAULT_TEST_RESULT_DETAIL_DURATION)
//                getMobileMeasurementResultDetailContainerResponse("key_num_threads", TestConstants.DEFAULT_TEST_NUM_THREADS.toString()),
//                getMobileMeasurementResultDetailContainerResponse("key_num_threads_ul", TestConstants.DEFAULT_TEST_NUM_THREADS_UPLOAD.toString()),
//                getMobileMeasurementResultDetailContainerResponse("key_tag", TestConstants.DEFAULT_TAG)
        ));
        return MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(properties)
                .build();
    }

    private MobileMeasurementResultDetailContainerResponse getMobileMeasurementResultDetailContainerResponse(String key, String value) {
        return MobileMeasurementResultDetailContainerResponse.builder()
                .title(messageSource.getMessage(key, null, Locale.ENGLISH))
                .value(value)
                .build();
    }

    private MobileMeasurementResultDetailResponse getDualSimResult() {
        var properties = getDefaultProperties();
        properties.addAll(List.of(
                getMobileMeasurementResultDetailContainerResponse("key_signal_strength", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SIGNAL_STRENGTH_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_signal_rsrp", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SIGNAL_RSRP_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_signal_rsrq", TestConstants.DEFAULT_TEST_RESULT_DETAIL_SIGNAL_RSRQ_VALUE),
                getMobileMeasurementResultDetailContainerResponse("key_network_type", TestConstants.DEFAULT_NETWORK_TYPE_LTE_RESULT_DETAIL_RESPONSE_VALUE)
//                getMobileMeasurementResultDetailContainerResponse("key_network_sim_operator_name", TestConstants.DEFAULT_TELEPHONY_NETWORK_SIM_OPERATOR_NAME),
//                getMobileMeasurementResultDetailContainerResponse("key_network_sim_operator", TestConstants.DEFAULT_TEST_NETWORK_SIM_OPERATOR),
//                getMobileMeasurementResultDetailContainerResponse("key_roaming", TestConstants.DEFAULT_ROAMING_TYPE_VALUE),
//                getMobileMeasurementResultDetailContainerResponse("key_network_operator_name", TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME)
        ));
        return MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(properties)
                .build();
    }

    private MobileMeasurementResultDetailResponse getTimeMobileMeasurementResultDetailResponse() {
        var timeResponse = MobileMeasurementResultDetailContainerResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIME_STRING)
                .title(messageSource.getMessage("key_time", null, Locale.ENGLISH))
                .timezone(TestConstants.DEFAULT_TIME_ZONE)
                .time(TestConstants.DEFAULT_TIME_INSTANT)
                .build();
        var timezoneResponse = MobileMeasurementResultDetailContainerResponse.builder()
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_TIMEZONE)
                .title(messageSource.getMessage("key_timezone", null, Locale.ENGLISH))
                .build();
        var networkTypeProperties = MobileMeasurementResultDetailContainerResponse.builder()
                .title(TestConstants.DEFAULT_NETWORK_TYPE_TITLE)
                .value(TestConstants.DEFAULT_NETWORK_TYPE_LTE_RESULT_DETAIL_RESPONSE_VALUE)
                .build();
        var testServerProperties = MobileMeasurementResultDetailContainerResponse.builder()
                .title(TestConstants.DEFAULT_TEST_SERVER_NAME_TITLE)
                .value(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME)
                .build();
        var properties = getDefaultProperties();
        properties.addAll(List.of(timeResponse, timezoneResponse, networkTypeProperties, testServerProperties));
        return MobileMeasurementResultDetailResponse.builder()
                .mobileMeasurementResultDetailContainerResponse(properties)
                .build();
    }

    private List<MobileMeasurementResultDetailContainerResponse> getDefaultProperties() {
        var openTestUUIDProperty = MobileMeasurementResultDetailContainerResponse.builder()
                .openTestUUID(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID)
                .title(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID_TITLE)
                .value(TestConstants.DEFAULT_TEST_RESULT_DETAIL_OPEN_TEST_UUID)
                .build();
        List<MobileMeasurementResultDetailContainerResponse> properties = new ArrayList<>();
        properties.add(openTestUUIDProperty);
        return properties;
    }

    private BasicTest.BasicTestBuilder getDefaultTestBuilder() {
        return BasicTest.builder()
                .measurementServerId(0L)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE_LTE_VALUE)
                .openTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
    }
}
