package com.specure.controller.core;

import com.specure.constant.URIConstants;
import com.specure.response.core.DataCollectorResponse;
import com.specure.response.core.IpResponse;
import com.specure.service.core.DataCollectorService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DataCollectorController {

    private final DataCollectorService dataCollectorService;

    @ApiOperation("Get data from request to client.")
    @GetMapping(value = {URIConstants.REQUEST_DATA_COLLECTOR, URIConstants.MOBILE + URIConstants.REQUEST_DATA_COLLECTOR})
    public DataCollectorResponse getDataCollector(HttpServletRequest request, @RequestHeader Map<String, String> headers) {
        return dataCollectorService.extrudeData(request, headers);
    }

    @ApiOperation(value = "Get ip from request")
    @PostMapping(value = {URIConstants.IP, URIConstants.MOBILE + URIConstants.IP})
    public IpResponse getClientIpVersion(HttpServletRequest httpServletRequest, @RequestHeader Map<String, String> headers) {
        return dataCollectorService.getIpVersion(httpServletRequest, headers);
    }
}
