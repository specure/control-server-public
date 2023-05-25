package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.response.neutrality.crud.NetNeutralitySettingResponse;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityTestResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NetNeutralityProcessorFactoryImplTest {
    @Mock
    private NetNeutralityProcessor netNeutralityProcessor;
    private NetNeutralityProcessorFactoryImpl netNeutralityProcessorFactory;

    @Mock
    private NetNeutralityResult netNeutralityResult;
    @Mock
    private NetNeutralityTestResultRequest netNeutralityTestResultRequest;
    @Mock
    private NetNeutralityResultResponse netNeutralityResultResponse;
    @Mock
    private NetNeutralitySetting netNeutralitySetting;
    @Mock
    private NetNeutralityTestParameterResponse netNeutralityTestParameterResponse;
    @Mock
    private NetNeutralitySettingResponse netNeutralitySettingResponse;
    @Mock
    private NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest;

    @BeforeEach
    void setUp() {
        when(netNeutralityProcessor.getType()).thenReturn(NetNeutralityTestType.DNS);
        netNeutralityProcessorFactory = new NetNeutralityProcessorFactoryImpl(List.of(netNeutralityProcessor));
    }

    @Test
    void toElasticModel_correctInvocation_expectedNetNeutralityResult() {
        when(netNeutralityTestResultRequest.getType()).thenReturn(NetNeutralityTestType.DNS);
        when(netNeutralityProcessor.toElasticModel(netNeutralityTestResultRequest, netNeutralityMeasurementResultRequest)).thenReturn(netNeutralityResult);

        var result = netNeutralityProcessorFactory.toElasticModel(netNeutralityTestResultRequest, netNeutralityMeasurementResultRequest);

        assertEquals(netNeutralityResult, result);
    }

    @Test
    void toElasticResponse_correctInvocation_expectedNetNeutralityResultResponse() {
        when(netNeutralityResult.getType()).thenReturn(NetNeutralityTestType.DNS);
        when(netNeutralityProcessor.toElasticResponse(netNeutralityResult)).thenReturn(netNeutralityResultResponse);

        var result = netNeutralityProcessorFactory.toElasticResponse(netNeutralityResult);

        assertEquals(netNeutralityResultResponse, result);
    }

    @Test
    void toNeutralityQosTestParameterResponse_correctInvocation_expectedNetNeutralityTestParameterResponse() {
        when(netNeutralitySetting.getType()).thenReturn(NetNeutralityTestType.DNS);
        when(netNeutralityProcessor.toNetNeutralityTestParameterResponse(netNeutralitySetting)).thenReturn(netNeutralityTestParameterResponse);

        var result = netNeutralityProcessorFactory.toNetNeutralityTestParameterResponse(netNeutralitySetting);

        assertEquals(netNeutralityTestParameterResponse, result);
    }

    @Test
    void toNetNeutralitySettingResponse_correctInvocation_expectedNetNeutralitySettingResponse() {
        when(netNeutralitySetting.getType()).thenReturn(NetNeutralityTestType.DNS);
        when(netNeutralityProcessor.toNetNeutralitySettingResponse(netNeutralitySetting)).thenReturn(netNeutralitySettingResponse);

        var result = netNeutralityProcessorFactory.toNetNeutralitySettingResponse(netNeutralitySetting);

        assertEquals(netNeutralitySettingResponse, result);
    }
}
