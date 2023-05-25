package com.specure.controller.core;

import com.specure.constant.URIConstants;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MeasurementResultController {
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Provide measurement result data for web client.")
    @PostMapping(URIConstants.TEST_RESULT)
    public void getMeasurementForWevClient() { }
}
