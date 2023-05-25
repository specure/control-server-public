package com.specure.controller.mobile;


import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.mobile.MobileQosMeasurementsRequest;
import com.specure.response.mobile.MobileMeasurementQosResponse;
import com.specure.response.mobile.OverallQosMeasurementResponse;
import com.specure.response.mobile.QosMeasurementsResponse;
import com.specure.service.mobile.MobileQosMeasurementService;
import com.specure.constant.URIConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@Api("Qos measurement")
@RestController
@RequiredArgsConstructor
@RequestMapping(URIConstants.MOBILE)
public class MobileQosMeasurementController {

    private final MobileQosMeasurementService mobileQosMeasurementService;

    @ApiOperation("Provide parameters for QoS measurements.")
    @PostMapping(URIConstants.MEASUREMENT_QOS_REQUEST)
    public MobileMeasurementQosResponse provideMeasurementQoSParameters(@Validated @RequestBody MeasurementQosParametersRequest measurementQosParametersRequest,
                                                                        @RequestHeader Map<String, String> headers) {
        return mobileQosMeasurementService.getQosParameters(measurementQosParametersRequest, headers);
    }

    @ApiOperation("Get QoS test results")
    @PostMapping(URIConstants.MEASUREMENT_QOS_RESULT)
    public QosMeasurementsResponse getQosTestResults(@Validated @RequestBody MobileQosMeasurementsRequest request) {
        return mobileQosMeasurementService.getQosResult(request.getTestUuid(), request.getLanguage(), request.getCapabilities());
    }

    @ApiOperation("Save QoS test results")
    @PostMapping(URIConstants.RESULT_QOS_URL)
    public OverallQosMeasurementResponse saveQosMeasurementResult(@RequestBody MeasurementQosRequest mobileQosResultRequest) {
        return mobileQosMeasurementService.saveQosMeasurementResult(mobileQosResultRequest);
    }

    @PostMapping(value = {URIConstants.QOS_BY_OPEN_TEST_UUID_AND_LANGUAGE, URIConstants.QOS_BY_OPEN_TEST_UUID})
    public QosMeasurementsResponse evaluateQosByOpenTestUUID(@PathVariable(name = "open_test_uuid") UUID openTestUUID, @PathVariable(name = "lang", required = false) String lang) {
        return mobileQosMeasurementService.evaluateQosByOpenTestUUID(openTestUUID, lang);
    }
}
