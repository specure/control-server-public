package com.specure.controller.sah;

import com.specure.constant.URIConstants;
import com.specure.request.core.MobileSettingsRequest;
import com.specure.request.core.SettingRequest;
import com.specure.response.core.settings.MobileSettingsResponse;
import com.specure.response.core.settings.SettingsResponse;
import com.specure.service.core.SettingsService;
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
public class SettingsController {

    final SettingsService settingsService;

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Provide settings for web client.")
    @PostMapping(value = {URIConstants.SETTINGS})
    public SettingsResponse getSettingsForWevClient(@Validated @RequestBody SettingRequest settingRequest) {
        return settingsService.getSettingsByRequest(settingRequest);
    }

    @PostMapping(value = {URIConstants.MOBILE + URIConstants.SETTINGS})
    @ApiOperation(value = "Get mobile settings of control server", notes = "Registers device, returns settings of the control server, map server and other settings and device UUID (creates new UUID when it is not sent in request body)")
    public MobileSettingsResponse getSettingsMobile(@RequestBody MobileSettingsRequest request) {
        return settingsService.getMobileSettings(request);
    }
}
