package com.specure.response.core.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.response.core.MeasurementServerResponseForSettings;
import com.specure.response.core.MeasurementSettingsResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MobileSettingResponse {

    @JsonProperty(value = "terms_and_conditions")
    private final TermAndConditionsResponse termAndConditionsResponse;

    private final UrlsResponse urls;

    @JsonProperty(value = "qostesttype_desc")
    private final List<QosMeasurementTypeDescription> qosTestTypeDescResponse;

    private final VersionsResponse versions;

    @JsonProperty(value = "servers")
    private final List<MeasurementServerResponseForSettings> servers;

    @JsonProperty(value = "servers_ws")
    private final List<MeasurementServerResponseForSettings> serverWSResponseList;

    @JsonProperty(value = "servers_qos")
    private final List<MeasurementServerResponseForSettings> serverQoSResponseList;

    private final HistorySettingsResponse history;

    private final String uuid;

    @JsonProperty(value = "map_server")
    private final MapServerSettingsResponse mapServerResponse;

    @JsonProperty(value = "measurement")
    private final MeasurementSettingsResponse measurement;
}
