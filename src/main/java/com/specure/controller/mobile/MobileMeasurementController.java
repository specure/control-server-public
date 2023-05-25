package com.specure.controller.mobile;

import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementResultDetailRequest;
import com.specure.request.mobile.MobileMeasurementResultRequest;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.response.mobile.MeasurementResultMobileResponse;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.response.mobile.MobileMeasurementResultContainerResponse;
import com.specure.response.mobile.MobileMeasurementResultDetailResponse;
import com.specure.service.mobile.MobileMeasurementRegistrationService;
import com.specure.service.mobile.MobileMeasurementService;
import com.specure.constant.URIConstants;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MobileMeasurementController {
    private final MobileMeasurementRegistrationService mobileMeasurementRegistrationService;
    private final MobileMeasurementService mobileMeasurementService;

    @PostMapping(URIConstants.MOBILE + URIConstants.RESULT)
    public MeasurementResultMobileResponse processResult(@RequestBody MeasurementResultMobileRequest measurementResultRequest, @RequestHeader Map<String, String> headers) {
        return mobileMeasurementService.processMeasurementResult(measurementResultRequest, headers);
    }

    @PostMapping(URIConstants.MOBILE + URIConstants.TEST_REQUEST)
    public MobileMeasurementRegistrationResponse registrationMeasurement(@Validated @RequestBody MobileMeasurementSettingRequest measurementServerMeasurePrepareRequest, @RequestHeader Map<String, String> headers, HttpServletRequest request) {
        return mobileMeasurementRegistrationService.registerMobileMeasurement(measurementServerMeasurePrepareRequest, headers, request);
    }

    @PostMapping(URIConstants.MOBILE + URIConstants.TEST_RESULT)
    @ApiOperation(value = "Get test result")
    @ResponseStatus(HttpStatus.OK)
    public MobileMeasurementResultContainerResponse getTestResultByTestUUID(@RequestBody MobileMeasurementResultRequest mobileMeasurementResultRequest) {
        return mobileMeasurementService.getTestResult(mobileMeasurementResultRequest);
    }

    @PostMapping(URIConstants.MOBILE + URIConstants.TEST_RESULT_DETAIL)
    @ApiOperation(value = "Get test result detail")
    @ResponseStatus(HttpStatus.OK)
    public MobileMeasurementResultDetailResponse getTestResultDetailByTestUUID(@RequestBody MobileMeasurementResultDetailRequest mobileMeasurementResultDetailRequest) {
        return mobileMeasurementService.getTestResultDetailByTestUUID(mobileMeasurementResultDetailRequest);
    }
}
