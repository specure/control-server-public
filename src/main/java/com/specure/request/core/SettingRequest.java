package com.specure.request.core;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SettingRequest {
    private String language;
    private String name;
    private boolean termsAndConditionsAccepted;
    private String timezone;
    private String type;
    private String versionCode;
    private String versionName;
}
