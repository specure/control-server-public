package com.specure.service.mobile.impl;

import com.specure.common.enums.TestType;
import com.specure.common.model.dto.TestResultCounter;
import com.specure.common.model.dto.qos.DnsResult;
import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.model.jpa.qos.DnsResultEntries;
import com.specure.common.model.jpa.qos.DnsTestResult;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.repository.MeasurementServerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.config.ApplicationProperties;
import com.specure.dto.sah.qos.QosTestDesc;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.dto.sah.qos.QosTestResult;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.mapper.mobile.MobileQosTestObjectiveMapper;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.repository.mobile.QosTestDescRepository;
import com.specure.repository.mobile.QosTestObjectiveRepository;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.measurement.qos.request.DnsResultEntriesRequest;
import com.specure.request.core.measurement.qos.request.DnsTestResultRequest;
import com.specure.request.core.measurement.qos.request.TestResult;
import com.specure.response.mobile.MobileMeasurementQosResponse;
import com.specure.response.mobile.MobileQosParamsResponse;
import com.specure.response.mobile.OverallQosMeasurementResponse;
import com.specure.response.mobile.QosMeasurementsResponse;
import com.specure.response.sah.ErrorContainerResponse;
import com.specure.sah.TestConstants;
import com.specure.service.BasicQosTestService;
import com.specure.service.mobile.MobileQosMeasurementService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.specure.sah.TestConstants.DEFAULT_LANGUAGE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MobileQosMeasurementServiceImplTest {
    private MobileQosMeasurementService qosMeasurementService;
    @MockBean
    private QosTestObjectiveRepository qosTestObjectiveRepository;
    @MockBean
    private MobileQosTestObjectiveMapper qosTestObjectiveMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private MeasurementQosRepository measurementQosRepository;
    @MockBean
    private MobileQosTestResultMapper mobileQosTestResultMapper;
    @MockBean
    private MeasurementQosMapper mobileMeasurementQosMapper;
    @MockBean
    private BasicQosTestService basicQosTestService;
    @MockBean
    private MeasurementServerRepository measurementServerRepository;
    @MockBean
    private MultiTenantManager multiTenantManager;
    ApplicationProperties applicationProperties = new ApplicationProperties(
            new ApplicationProperties.LanguageProperties(Set.of("en", "de"), "en"),
            Set.of("RMBT", "RMBTjs", "Open-RMBT", "RMBTws", "HW-PROBE"),
            "0.1.0 || 0.3.0 || ^1.0.0",
            1,
            2,
            3,
            10000,
            2000
    );
    private static final Measurement test = Measurement.builder()
            .openTestUuid(TestConstants.DEFAULT_UUID)
            .build();
    private static final HashSet<DnsResult.DnsEntry> dnsEntries = new HashSet<>();
    private static final DnsResult.DnsEntry dnsEntry = DnsResult.DnsEntry.builder().address("addr").priority(((short) 1)).build();
    private static final DnsResult dnsResult = DnsResult.builder()
            .duration(1)
            .entriesFound(2)
            .host("host")
            .record("record")
            .info("info")
            .resultEntries(dnsEntries)
            .resolver("Resolver")
            .status("DONE")
            .timeout(100)
            .build();
    TestResult qosSendTestResultItem = DnsTestResultRequest.builder()
            .qosTestUid(TestConstants.DEFAULT_UID)
            .dnsResultDuration(1L)
            .dnsResultInfo("dnsResultInfo")
            .dnsObjectiveHost("dnsObjectiveHost")
            .dnsResultStatus("dnsResultStatus")
            .dnsObjectiveResolver("dnsObjectiveResolver")
            .dnsObjectiveTimeout(1L)
            .dnsResultEntriesFound(1)
            .dnsObjectiveDnsRecord("dnsObjectiveDnsRecord")
            .dnsResultEntries(List.of(DnsResultEntriesRequest.builder().dnsResultAddress("dnsResultAddress").dnsResultTtl("101").build()))
            .build();
    DnsTestResult dnsTestResult = DnsTestResult.builder()
            .id(TestConstants.DEFAULT_ID)
            .qosTestUid(TestConstants.DEFAULT_UID)
            .successCount(TestConstants.DEFAULT_COUNT)
            .failureCount(TestConstants.DEFAULT_COUNT)
            .dnsResultDuration(1L)
            .dnsResultInfo("dnsResultInfo")
            .dnsObjectiveHost("dnsObjectiveHost")
            .dnsResultStatus("dnsResultStatus")
            .dnsObjectiveResolver("dnsObjectiveResolver")
            .dnsObjectiveTimeout(1L)
            .dnsResultEntriesFound(1)
            .dnsObjectiveDnsRecord("dnsObjectiveDnsRecord")
            .dnsResultEntries(List.of(DnsResultEntries.builder().dnsResultAddress("dnsResultAddress").dnsResultTtl("100").build()))
            .build();
    QosTestResult dnsQosTestResult = QosTestResult.builder()
            .id(dnsTestResult.getId())
            .successCount(dnsTestResult.getSuccessCount())
            .failureCount(dnsTestResult.getFailureCount())
            .qosTestObjectiveId(dnsTestResult.getQosTestUid())
            .result(objectMapper.writeValueAsString(dnsTestResult))
            .build();
    MeasurementQosRequest defaultQosResultRequest = MeasurementQosRequest.builder()
            .testToken(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN + "_QOS")
            .clientLanguage(TestConstants.DEFAULT_LANGUAGE)
            .clientVersion(TestConstants.DEFAULT_CLIENT_VERSION)
            .clientName(TestConstants.DEFAULT_CLIENT_NAME)
            .qosResult(List.of(qosSendTestResultItem))
            .build();
    QosTestObjective qosTestObjective = QosTestObjective.builder()
            .testType(TestConstants.DEFAULT_TEST_TYPE)
            .build();
    @Mock
    private QosTestDescRepository qosTestDescRepository;
    @Mock
    private QosTestObjective qosTestObjectiveFirst;
    @Mock
    private QosTestObjective qosTestObjectiveSecond;
    @Mock
    private MobileQosParamsResponse qosParamsResponseFirst;
    @Mock
    private MobileQosParamsResponse qosParamsResponseSecond;
    @Mock
    private MeasurementRepository testRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private MeasurementQosParametersRequest measurementQosParametersRequest;
    @Mock
    private QosMeasurementsResponse.QosTestResultItem qosTestResultItem;
    @Mock
    private MeasurementServer measurementServer;
    @Mock
    private BasicQosTest basicQosTest;
    private Map<String, String> headers;

    private MeasurementQos measurementQos = MeasurementQos.builder()
            .tcpTestResults(Collections.emptyList())
            .httpProxyTestResults(Collections.emptyList())
            .udpTestResults(Collections.emptyList())
            .voipTestResults(Collections.emptyList())
            .nonTransparentProxyTestResults(Collections.emptyList())
            .tracerouteTestResults(Collections.emptyList())
            .websiteTestResults(Collections.emptyList())
            .dnsTestResults(List.of(dnsTestResult))
            .build();

    public MobileQosMeasurementServiceImplTest() throws JsonProcessingException {
    }

    @Before
    public void setUp() throws Exception {
        qosMeasurementService = new MobileQosMeasurementServiceImpl(
                qosTestObjectiveRepository,
                qosTestObjectiveMapper,
                applicationProperties,
                testRepository,
                messageSource,
                objectMapper,
                mobileQosTestResultMapper,
                qosTestDescRepository,
                basicQosTestService,
                measurementQosRepository,
                mobileMeasurementQosMapper,
                measurementServerRepository,
                multiTenantManager
        );
        headers = new HashMap<>();
        dnsEntries.add(dnsEntry);

        qosTestObjective.setResults(objectMapper.writeValueAsString(List.of(dnsResult)));
    }

    @Test
    public void getQosParameters_whenCommonData_expectMeasurementQosResponse() {
        var expectedResponse = getMeasurementQosResponse();
        when(measurementQosParametersRequest.getUuid()).thenReturn(TestConstants.DEFAULT_CLIENT_UUID_STRING);
        when(measurementServerRepository.findByClientUUID(TestConstants.DEFAULT_CLIENT_UUID_STRING)).thenReturn(Optional.of(measurementServer));
        when(qosTestObjectiveRepository.findAll()).thenReturn(List.of(qosTestObjectiveFirst, qosTestObjectiveSecond));
        when(qosTestObjectiveFirst.getTestType()).thenReturn(TestConstants.DEFAULT_TEST_TYPE);
        when(qosTestObjectiveSecond.getTestType()).thenReturn(TestConstants.DEFAULT_TEST_TYPE);
        when(qosTestObjectiveMapper.qosTestObjectiveToQosParamsResponse(qosTestObjectiveFirst)).thenReturn(qosParamsResponseFirst);
        when(qosTestObjectiveMapper.qosTestObjectiveToQosParamsResponse(qosTestObjectiveSecond)).thenReturn(qosParamsResponseSecond);

        var response = qosMeasurementService.getQosParameters(measurementQosParametersRequest, headers);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void saveQosMeasurementResult_whenCommonData_expectEmptyErrorResponse() throws JsonProcessingException, InterruptedException {
        QosTestResult qosTestResult = new QosTestResult();
        qosTestResult.setResult(objectMapper.writeValueAsString(
                qosSendTestResultItem.toBuilder()
                        .test_type(null)
                        .qosTestUid(0L)
                        .build()
        ));
        qosTestResult.setTestUid(test.getId());
        qosTestResult.setQosTestObjective(qosTestObjective);

        when(testRepository.findByOpenTestUuid(TestConstants.DEFAULT_UUID)).thenReturn(Optional.of(test));
        when(qosTestObjectiveRepository.getOne(qosSendTestResultItem.getQosTestUid())).thenReturn(qosTestObjective);
        when(measurementQosRepository.findByOpenTestUuid(test.getOpenTestUuid())).thenReturn(Optional.of(measurementQos));
        when(mobileMeasurementQosMapper.measurementQosRequestToMeasurementQosMobile(defaultQosResultRequest)).thenReturn(measurementQos);
        when(basicQosTestService.saveMeasurementQosMobileToElastic(measurementQos)).thenReturn(basicQosTest);
        when(basicQosTest.getOverallQos()).thenReturn(TestConstants.DEFAULT_OVERALL_QOS);
        when(basicQosTest.getQosTestResultCounters()).thenReturn(getTestResultCounterDtoList());

        OverallQosMeasurementResponse response = qosMeasurementService.saveQosMeasurementResult(defaultQosResultRequest);
        Thread.sleep(100);
        assertTrue(response.getError().isEmpty());
        assertEquals(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE, response.getOverallQos());
        assertEquals(getTestResultCounterDtoList(), response.getQosTestResultCounters());
        verify(measurementQosRepository).save(measurementQos);
        verify(basicQosTestService).saveMeasurementQosMobileToElastic(measurementQos);
    }

    @Test
    @Ignore
    public void saveQosMeasurementResult_whenNoTestFound_expectNothingSaved() {
        ErrorContainerResponse response = qosMeasurementService.saveQosMeasurementResult(defaultQosResultRequest);

        assertTrue(response.getError().isEmpty());
        verify(measurementQosRepository, never()).save(any());
    }

    @Test
    public void saveQosMeasurementResult_whenClientVersionIsNotValid_expectNothingSaved() {
        MeasurementQosRequest qosResultRequest = defaultQosResultRequest.toBuilder()
                .clientVersion("abc")
                .build();

        when(testRepository.findByOpenTestUuid(TestConstants.DEFAULT_UUID)).thenReturn(Optional.of(test));
        when(messageSource.getMessage("ERROR_CLIENT_VERSION", null, Locale.ENGLISH)).thenReturn("ERROR_CLIENT_VERSION");
        ErrorContainerResponse response = qosMeasurementService.saveQosMeasurementResult(qosResultRequest);

        assertEquals(1, response.getError().size());
        assertTrue(response.getError().contains("ERROR_CLIENT_VERSION"));
        verify(measurementQosRepository, never()).save(any());
    }

    @Test
    public void saveQosMeasurementResult_whenTestTokenInvalid_expectNothingSaved() {
        MeasurementQosRequest qosResultRequest = defaultQosResultRequest.toBuilder()
                .testToken(TestConstants.DEFAULT_TEXT)
                .build();

        when(messageSource.getMessage("ERROR_TEST_TOKEN_MALFORMED", null, Locale.ENGLISH)).thenReturn("ERROR_TEST_TOKEN_MALFORMED");
        ErrorContainerResponse response = qosMeasurementService.saveQosMeasurementResult(qosResultRequest);

        assertEquals(1, response.getError().size());
        assertTrue(response.getError().contains("ERROR_TEST_TOKEN_MALFORMED"));
        verify(measurementQosRepository, never()).save(any());
    }

    @Test
    public void saveQosMeasurementResult_whenTestTokenMissing_expectNothingSaved() {
        MeasurementQosRequest qosResultRequest = defaultQosResultRequest.toBuilder()
                .testToken("")
                .build();

        when(messageSource.getMessage("ERROR_TEST_TOKEN_MISSING", null, Locale.ENGLISH)).thenReturn("ERROR_TEST_TOKEN_MISSING");
        ErrorContainerResponse response = qosMeasurementService.saveQosMeasurementResult(qosResultRequest);

        assertEquals(1, response.getError().size());
        assertTrue(response.getError().contains("ERROR_TEST_TOKEN_MISSING"));
        verify(measurementQosRepository, never()).save(any());
    }

    @Test
    public void getQosResult_whenCommonData_expectQosResults() {
        QosTestDesc qosTestDesc = new QosTestDesc(1L, "timeout", "Timeout", DEFAULT_LANGUAGE);

        when(testRepository.findByOpenTestUuid(TestConstants.DEFAULT_UUID)).thenReturn(Optional.of(test));
        when(measurementQosRepository.findByOpenTestUuid(test.getOpenTestUuid())).thenReturn(Optional.of(measurementQos));
        when(qosTestDescRepository.findByKeysAndLocales(eq(DEFAULT_LANGUAGE), eq(applicationProperties.getLanguage().getSupportedLanguages()), any()))
                .thenReturn(List.of(qosTestDesc));
        when(mobileQosTestResultMapper.testResultToQosTestResult(dnsTestResult)).thenReturn(dnsQosTestResult);
        when(qosTestObjectiveRepository.getOne(TestConstants.DEFAULT_ID)).thenReturn(qosTestObjective);
        QosMeasurementsResponse result = qosMeasurementService.getQosResult(UUID.fromString(TestConstants.DEFAULT_UUID), DEFAULT_LANGUAGE, null);

        assertNotNull(result.getEvalTimes());
        assertTrue(result.getError().isEmpty());
    }

    @Test
    public void evaluateQosByOpenTestUUID_whenCommonData_expectQosMeasurementsResponse() {
        when(testRepository.findByOpenTestUuid(TestConstants.DEFAULT_UUID)).thenReturn(Optional.of(test));
        when(measurementQosRepository.findByOpenTestUuid(test.getOpenTestUuid())).thenReturn(Optional.of(measurementQos));
        when(mobileQosTestResultMapper.testResultToQosTestResult(dnsTestResult)).thenReturn(dnsQosTestResult);
        when(qosTestObjectiveRepository.getOne(TestConstants.DEFAULT_ID)).thenReturn(qosTestObjective);
        when(mobileQosTestResultMapper.toQosTestResultItem(eq(dnsQosTestResult), anyBoolean())).thenReturn(qosTestResultItem);

        QosMeasurementsResponse result = qosMeasurementService.evaluateQosByOpenTestUUID(UUID.fromString(TestConstants.DEFAULT_UUID), DEFAULT_LANGUAGE);

        assertNotNull(result.getEvalTimes());
        assertEquals(List.of(qosTestResultItem), result.getTestResultDetails());
        assertTrue(result.getError().isEmpty());
    }


    private MobileMeasurementQosResponse getMeasurementQosResponse() {
        Map<TestType, List<MobileQosParamsResponse>> objectives = new HashMap<>();
        objectives.put(TestConstants.DEFAULT_TEST_TYPE, List.of(qosParamsResponseFirst, qosParamsResponseSecond));

        return MobileMeasurementQosResponse.builder()
                .testNumPings(applicationProperties.getPings())
                .testDuration(applicationProperties.getDuration())
                .testNumThreads(applicationProperties.getThreads())
                .objectives(objectives)
                .error(Collections.emptyList())
                .build();
    }

    private List<TestResultCounter> getTestResultCounterDtoList() {
        return List.of(
                TestResultCounter.builder()
                        .testType(TestConstants.DEFAULT_TEST_TYPE)
                        .successCount(TestConstants.DEFAULT_SUCCESS_COUNT)
                        .totalCount(TestConstants.DEFAULT_TOTAL_COUNT)
                        .build()
        );
    }
}
