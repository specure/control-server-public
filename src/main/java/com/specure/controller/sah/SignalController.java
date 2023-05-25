package com.specure.controller.sah;


import com.specure.constant.URIConstants;
import com.specure.request.sah.SignalRequest;
import com.specure.response.sah.SignalResponse;
import com.specure.service.sah.SignalService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignalController {

    private final SignalService signalService;

    @PostMapping(value = {URIConstants.SIGNAL_REQUEST, URIConstants.MOBILE + URIConstants.SIGNAL_REQUEST})
    @ApiOperation(value = "Register signal", notes = "Request to obtain configuration for signal monitoring")
    @ResponseStatus(HttpStatus.CREATED)
    public SignalResponse registerSignal(HttpServletRequest httpServletRequest, @RequestBody @Valid SignalRequest signalRequest) {
        return signalService.registerSignal(signalRequest, httpServletRequest);
    }
}
