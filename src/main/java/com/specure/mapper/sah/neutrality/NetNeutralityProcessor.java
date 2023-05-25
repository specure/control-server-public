package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.response.neutrality.crud.NetNeutralitySettingResponse;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityTestResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;

public interface NetNeutralityProcessor {

    NetNeutralityResult toElasticModel(NetNeutralityTestResultRequest neutralityQosTestResultRequest, NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest);

    NetNeutralityResultResponse toElasticResponse(NetNeutralityResult neutralityQosTest);

    NetNeutralityTestType getType();

    NetNeutralityTestParameterResponse toNetNeutralityTestParameterResponse(NetNeutralitySetting neutralityQosTestJpa);

    NetNeutralitySettingResponse toNetNeutralitySettingResponse(NetNeutralitySetting netNeutralitySetting);
}
