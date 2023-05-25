package com.specure.controller.sah;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.constant.URIConstants;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityOverallTestResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import com.specure.service.sah.NetNeutralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class NetNeutralityTestController {

    private final NetNeutralityService netNeutralityService;

    @GetMapping(URIConstants.NET_NEUTRALITY_TEST_REQUEST)
    public Map<NetNeutralityTestType, List<NetNeutralityTestParameterResponse>> provideMeasurementNetNeutralityParameters() {
        return netNeutralityService.getQosParameters();
    }

    @GetMapping(URIConstants.NET_NEUTRALITY_RESULT_BY_UUID)
    public Page<NetNeutralityResultResponse> getNetNeutralityResultByClientUuid(@RequestParam String uuid,
                                                                                @RequestParam(required = false) String openTestUuid,
                                                                                @PageableDefault(size = 1000) Pageable pageable) {
        return netNeutralityService.getNetNeutralityResultByClientUuid(uuid, openTestUuid, pageable);
    }

    @PostMapping(URIConstants.NET_NEUTRALITY_RESULT)
    public NetNeutralityOverallTestResultResponse saveNetNeutralityResult(@Valid @RequestBody NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        return netNeutralityService.saveNetNeutralityMeasurementResult(netNeutralityMeasurementResultRequest);
    }
}
