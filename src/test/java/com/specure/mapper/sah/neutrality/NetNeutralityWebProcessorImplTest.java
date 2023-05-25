package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.MobileModel;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.common.repository.NetNeutralitySettingRepository;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.specure.enums.NetNeutralityStatus;
import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.sah.TestConstants;
import com.specure.sah.TestObjects;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
import com.specure.service.sah.NationalOperatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NetNeutralityWebProcessorImplTest {

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private NetNeutralitySettingRepository netNeutralitySettingRepository;
    @Mock
    private Clock clock;
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @Mock
    private NationalOperatorService nationalOperatorService;
    @Mock
    private SimOperatorRepository simOperatorRepository;
    @Mock
    private RawProviderService rawProviderService;
    @Mock
    private DiggerService diggerService;
    @Mock
    private MobileModelService mobileModelService;
    @Mock
    private RadioSignalMapper radioSignalMapper;
    @InjectMocks
    private NetNeutralityWebProcessorImpl netNeutralityWebProcessor;
    @Mock
    private RawProvider rawProvider;
    @Mock
    private MobileModel mobileModel;

    @BeforeEach
    void setUp() {
        when(uuidGenerator.generateUUID()).thenReturn(UUID.fromString(TestConstants.NetNeutrality.WEB_UUID));
        when(clock.instant()).thenReturn(Instant.ofEpochMilli(TestConstants.NetNeutrality.WEB_TIMESTAMP_INSTANT_MS));
        when(radioSignalMapper.radioSignalRequestToRadioSignalNN(any())).thenReturn(TestObjects.radioSignalResponse());
    }

    @Test
    void toElasticModel_actualStatusEqualToExpectedStatusAndTimeoutNotExceeded_expectedNetNeutralityWebSettingAndStatusSUCCEED() {
        var expectedStatusCode = 500L;
        var actualStatusCode = 500L;
        var isTimeoutExceeded = false;
        var request = TestObjects.netNeutralityWebTestResultRequest(actualStatusCode, isTimeoutExceeded);
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityWebResult(NetNeutralityStatus.SUCCEED, actualStatusCode, expectedStatusCode, isTimeoutExceeded);
        var netNeutralityWebSetting = TestObjects.netNeutralityWebSetting(expectedStatusCode);
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.WEB_ID, NetNeutralityTestType.WEB)).thenReturn(Optional.of(netNeutralityWebSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);


        var result = netNeutralityWebProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_actualStatusEqualToExpectedStatusAndTimeoutIsExceeded_expectedNetNeutralityWebSettingAndStatusFAIL() {
        var expectedStatusCode = 500L;
        var actualStatusCode = 500L;
        var isTimeoutExceeded = true;
        var request = TestObjects.netNeutralityWebTestResultRequest(actualStatusCode, isTimeoutExceeded);
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityWebResult(NetNeutralityStatus.FAIL, actualStatusCode, expectedStatusCode, isTimeoutExceeded);
        var netNeutralityWebSetting = TestObjects.netNeutralityWebSetting(expectedStatusCode);
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.WEB_ID, NetNeutralityTestType.WEB)).thenReturn(Optional.of(netNeutralityWebSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);


        var result = netNeutralityWebProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_actualStatusDifferFromExpectedStatusAndTimeoutNotExceeded_expectedNetNeutralityWebSettingAndStatusFAIL() {
        var expectedStatusCode = 500L;
        var actualStatusCode = 200L;
        var isTimeoutExceeded = false;
        var request = TestObjects.netNeutralityWebTestResultRequest(actualStatusCode, isTimeoutExceeded);
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityWebResult(NetNeutralityStatus.FAIL, actualStatusCode, expectedStatusCode, isTimeoutExceeded);
        var netNeutralityWebSetting = TestObjects.netNeutralityWebSetting(expectedStatusCode);
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.WEB_ID, NetNeutralityTestType.WEB)).thenReturn(Optional.of(netNeutralityWebSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);


        var result = netNeutralityWebProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticResponse_correctInvocation_expectedNetNeutralityResultResponse() {
        var netNeutralityWebResult = TestObjects.netNeutralityWebResult();
        var netNeutralityResultResponse = TestObjects.netNeutralityWebResultResponse();

        var result = netNeutralityWebProcessor.toElasticResponse(netNeutralityWebResult);

        assertEquals(netNeutralityResultResponse, result);
    }

    @Test
    void getType_correctInvocation_expectedWeb() {
        assertEquals(NetNeutralityTestType.WEB, netNeutralityWebProcessor.getType());
    }

    @Test
    void toNetNeutralityTestParameterResponse_correctInvocation_expectedNetNeutralityTestParameterResponse() {
        var netNeutralityWebSetting = TestObjects.netNeutralityWebSetting();
        var netNeutralityWebTestParametersResponse = TestObjects.netNeutralityWebTestParametersResponse();

        var result = netNeutralityWebProcessor.toNetNeutralityTestParameterResponse(netNeutralityWebSetting);

        assertEquals(netNeutralityWebTestParametersResponse, result);
    }

    @Test
    void toNetNeutralitySettingResponse_correctInvocation_expectedNetNeutralitySettingResponse() {
        var netNeutralityWebSetting = TestObjects.netNeutralityWebSetting();
        var expectedResult = TestObjects.netNeutralityWebSettingResponse();

        var result = netNeutralityWebProcessor.toNetNeutralitySettingResponse(netNeutralityWebSetting);

        assertEquals(expectedResult, result);
    }
}
