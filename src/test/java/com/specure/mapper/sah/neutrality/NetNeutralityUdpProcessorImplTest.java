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
import com.specure.model.elastic.GeoShape;
import com.specure.sah.TestConstants;
import com.specure.sah.TestObjects;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
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
class NetNeutralityUdpProcessorImplTest {

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private NetNeutralitySettingRepository netNeutralitySettingRepository;
    @Mock
    private Clock clock;
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @Mock
    private RawProviderService rawProviderService;
    @Mock
    private MobileModelService mobileModelService;
    @Mock
    private RadioSignalMapper radioSignalMapper;
    @Mock
    private DiggerService diggerService;

    @InjectMocks
    private NetNeutralityUdpProcessorImpl netNeutralityUdpProcessor;
    @Mock
    private RawProvider rawProvider;
    @Mock
    private MobileModel mobileModel;

    @BeforeEach
    void setUp() {
        when(uuidGenerator.generateUUID()).thenReturn(UUID.fromString(TestConstants.NetNeutrality.UDP_UUID));
        when(clock.instant()).thenReturn(Instant.ofEpochMilli(TestConstants.NetNeutrality.UDP_TIMESTAMP_INSTANT_MS));
        when(radioSignalMapper.radioSignalRequestToRadioSignalNN(any())).thenReturn(TestObjects.radioSignalResponse());
    }

    @Test
    void toElasticModel_whenNumberOfPacketsReceivedMoreThanMinNumberOfPacketsReceived_expectedSUCCEED() {
        Long actualNumberOfPacketsReceived = 6L;
        Long expectedNumberOfPacketsReceived = 5L;
        var netNeutralityUdpSetting = TestObjects.netNeutralityUdpSetting(expectedNumberOfPacketsReceived);
        var netNeutralityUdpTestResultRequest = TestObjects.netNeutralityUdpTestResultRequest(actualNumberOfPacketsReceived);
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(netNeutralityUdpTestResultRequest));
        var netNeutralityUdpResult = TestObjects.netNeutralityUdpResult(NetNeutralityStatus.SUCCEED, actualNumberOfPacketsReceived, expectedNumberOfPacketsReceived);
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.UDP_ID, NetNeutralityTestType.UDP)).thenReturn(Optional.of(netNeutralityUdpSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);


        var result = netNeutralityUdpProcessor.toElasticModel(netNeutralityUdpTestResultRequest, netNeutralityMeasurementResultRequest);

        assertEquals(netNeutralityUdpResult, result);
    }

    @Test
    void getType_correctInvocation_expectedUdp() {
        assertEquals(NetNeutralityTestType.UDP, netNeutralityUdpProcessor.getType());
    }

    @Test
    void toElasticResponse_correctInvocation_expectedNetNeutralityResultResponse() {
        GeoShape geoShape = GeoShape.builder()
                .countySq(TestConstants.DEFAULT_COUNTY)
                .build();
        when(geoLocationRepository.getGeoShapeByCoordinates(TestConstants.DEFAULT_LONGITUDE, TestConstants.DEFAULT_LATITUDE)).thenReturn(Optional.of(geoShape));
        var netNeutralityUdpResult = TestObjects.netNeutralityUdpResult();
        var netNeutralityResultResponse = TestObjects.netNeutralityUdpResultResponse();

        var result = netNeutralityUdpProcessor.toElasticResponse(netNeutralityUdpResult);

        assertEquals(netNeutralityResultResponse, result);
    }

    @Test
    void toNetNeutralityTestParameterResponse_correctInvocation_expectedNetNeutralityTestParameterResponse() {
        var netNeutralityUdpSetting = TestObjects.netNeutralityUdpSetting();
        var netNeutralityUdpTestParametersResponse = TestObjects.netNeutralityUdpTestParametersResponse();

        var result = netNeutralityUdpProcessor.toNetNeutralityTestParameterResponse(netNeutralityUdpSetting);

        assertEquals(netNeutralityUdpTestParametersResponse, result);
    }

    @Test
    void toNetNeutralitySettingResponse_correctInvocation_expectedNetNeutralitySettingResponse() {
        var netNeutralityUdpSetting = TestObjects.netNeutralityUdpSetting();
        var expectedResult = TestObjects.netNeutralityUdpSettingResponse();

        var result = netNeutralityUdpProcessor.toNetNeutralitySettingResponse(netNeutralityUdpSetting);

        assertEquals(expectedResult, result);
    }
}
