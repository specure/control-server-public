package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.response.neutrality.crud.NetNeutralitySettingResponse;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityTestResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NetNeutralityProcessorFactoryImpl {

    private final Map<NetNeutralityTestType, NetNeutralityProcessor> MAPPERS = new EnumMap<>(NetNeutralityTestType.class);

    public NetNeutralityProcessorFactoryImpl(List<NetNeutralityProcessor> mappers) {
        mappers
                .forEach(mapper -> MAPPERS.put(mapper.getType(), mapper));
    }

    public NetNeutralityResult toElasticModel(NetNeutralityTestResultRequest netNeutralityTestResultRequest, NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        return MAPPERS.get(netNeutralityTestResultRequest.getType()).toElasticModel(netNeutralityTestResultRequest, netNeutralityMeasurementResultRequest);
    }

    public NetNeutralityResultResponse toElasticResponse(NetNeutralityResult netNeutralityResult) {
        return MAPPERS.get(netNeutralityResult.getType()).toElasticResponse(netNeutralityResult);
    }

    public NetNeutralityTestParameterResponse toNetNeutralityTestParameterResponse(NetNeutralitySetting netNeutralitySetting) {
        return MAPPERS.get(netNeutralitySetting.getType()).toNetNeutralityTestParameterResponse(netNeutralitySetting);
    }

    public NetNeutralitySettingResponse toNetNeutralitySettingResponse(NetNeutralitySetting netNeutralitySetting) {
        return MAPPERS.get(netNeutralitySetting.getType()).toNetNeutralitySettingResponse(netNeutralitySetting);
    }
}
