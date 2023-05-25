package com.specure.response.core.settings;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MobileSettingsResponse {

    private final List<MobileSettingResponse> settings;
}
