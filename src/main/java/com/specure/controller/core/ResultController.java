package com.specure.controller.core;

import com.specure.request.core.MeasurementResultRequest;
import com.specure.constant.URIConstants;
import com.specure.response.core.measurement.result.MeasurementResultWebClientResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ResultController {

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Provide settings for web client.")
    @PostMapping(URIConstants.RESULT)
    public MeasurementResultWebClientResponse getMeasurementResult(@Validated @RequestBody MeasurementResultRequest measurementResultRequest) {
        return MeasurementResultWebClientResponse.builder().build();
    }
}
