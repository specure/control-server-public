package com.specure.service.sah;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityOverallTestResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NetNeutralityService {

    Map<NetNeutralityTestType, List<NetNeutralityTestParameterResponse>> getQosParameters();

    NetNeutralityOverallTestResultResponse saveNetNeutralityMeasurementResult(NetNeutralityMeasurementResultRequest neutralityQosTestResultRequest);

    Page<NetNeutralityResultResponse> getNetNeutralityResultByClientUuid(String uuid, String openTestUuid, Pageable pageable);
}
