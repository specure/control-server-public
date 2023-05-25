package com.specure.response.core.settings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SettingsResponse {
    private List<SettingResponse> settings;
    private final List<String> errors;
}
