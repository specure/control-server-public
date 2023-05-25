package com.specure.response.core.settings;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MapServerSettingsResponse {
    private String host;
    private Long port;
    private boolean ssl;
}
