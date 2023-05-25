package com.specure.response.mobile;

import com.specure.common.model.dto.TestResultCounter;
import com.specure.common.response.LocationResponse;
import com.specure.common.response.userexperience.UserExperienceMetric;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.response.sah.RadioSignalResponse;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BasicTestHistoryMobileResponse {

    @JsonProperty("test_uuid")
    private final String openTestUuid;

    @JsonProperty("speed_upload")
    private final Integer uploadSpeed;

    @JsonProperty("speed_download")
    private final Integer downloadSpeed;

    private final Float ping;

    @JsonProperty("voip_result_jitter_millis")
    private final Float jitter;

    @JsonProperty("voip_result_packet_loss_percents")
    private final Float packetLoss;

    @JsonProperty("network_type")
    private final String networkType;

    @JsonProperty(value = "qos")
    private final Float qos;

    @JsonProperty(value = "qosTestResultCounters")
    private final List<TestResultCounter> qosTestResultCounters;

    @JsonProperty("measurement_date")
    private final String measurementDate;

    @JsonProperty(value = "device")
    private final String device;

    @JsonProperty(value = "loop_mode_uuid")
    private final String loopModeUuid;

    @JsonProperty(value = "userExperienceMetrics")
    private final List<UserExperienceMetric> userExperienceMetrics;

    @JsonProperty(value = "radioSignals")
    private final List<RadioSignalResponse> radioSignals;

    @JsonProperty(value = "networkName")
    private final String networkName;

    @JsonProperty(value = "measurementServerName")
    private final String measurementServerName;

    @JsonProperty(value = "location")
    private final LocationResponse location;

    @JsonProperty(value = "measurementServerCity")
    private final String measurementServerCity;

    @JsonProperty(value = "operator")
    private final String operator;

    @JsonProperty(value = "platform")
    private final String platform;

    @JsonProperty(value = "appVersion")
    private final String appVersion;
}
