package com.specure.service.core;

import com.specure.request.core.MobileSettingsRequest;
import com.specure.request.core.SettingRequest;
import com.specure.response.core.settings.MobileSettingsResponse;
import com.specure.response.core.settings.SettingsResponse;

import java.util.Map;

public interface SettingsService {
    SettingsResponse getSettingsByRequest(SettingRequest settingRequest);

    MobileSettingsResponse getMobileSettings(MobileSettingsRequest mobileSettingsRequest);

    Map<String, String> getSettingsMap();
}
