package com.specure.response.core.settings;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SurveySettingsResponse {
    private long dateStarted;
    private boolean isActiveService;
    private String surveyUrl;
}
