package com.specure.controller.core;

import com.specure.constant.URIConstants;
import com.specure.request.core.ClientLocationRequest;
import com.specure.response.core.NearestMeasurementServersResponse;
import com.specure.service.core.MeasurementServerService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MeasurementServerController {
    @Qualifier("basicMeasurementServerService")
    private final MeasurementServerService basicMeasurementServerService;

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get list of measurement servers for web client")
    @PostMapping(URIConstants.MEASUREMENT_SERVER_WEB)
    public NearestMeasurementServersResponse getMeasurementServersForWebClient(@Validated @RequestBody ClientLocationRequest clientLocationRequest) {
        return basicMeasurementServerService.getNearestServers(clientLocationRequest);
    }
}
