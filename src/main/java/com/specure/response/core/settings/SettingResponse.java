package com.specure.response.core.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SettingResponse {
    private List<AdvertisedSpeedOption> advertisedSpeedOption;
    private HistorySettingsResponse history;
    private MapServerSettingsResponse mapServer;

    @JsonProperty("qostesttype_desc")
    private List<QosMeasurementTypeDescription> qosTestTypeDesc;

    private SurveySettingsResponse surveySettings;
    private String uuid;
    private UrlsResponse urls;
    private VersionsResponse versions;
}
