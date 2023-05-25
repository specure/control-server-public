package com.specure.controller.sah;


import com.specure.response.sah.HealthResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.specure.constant.URIConstants.HEALTH;

@RequiredArgsConstructor
@RestController
public class HealthCheckerController {


    @ApiOperation("Get health information.")
    @GetMapping(HEALTH)
    public HealthResponse getGraphs() {
        return HealthResponse.builder()
                .lastDownPingSignal(1)
                .build();
    }
}
