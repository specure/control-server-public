package com.specure.controller.sah;

import com.specure.constant.URIConstants;
import com.specure.response.sah.version.ApplicationVersionResponse;
import com.specure.service.sah.ApplicationVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VersionController {

    private final ApplicationVersionService applicationVersionService;

    @GetMapping(URIConstants.VERSION)
    public ApplicationVersionResponse getApplicationVersion() throws IOException {
        return applicationVersionService.getApplicationVersion();
    }
}
