package com.specure.controller.mobile;

import com.specure.response.mobile.MobileGraphResponse;
import com.specure.service.mobile.MobileGraphService;
import com.specure.constant.URIConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MobileGraphController {

    private final MobileGraphService mobileGraphService;

    @GetMapping(URIConstants.MOBILE + URIConstants.GRAPHS + URIConstants.UUID)
    public MobileGraphResponse processResult(@PathVariable String uuid) {
        return mobileGraphService.getMobileGraph(uuid);
    }

}
