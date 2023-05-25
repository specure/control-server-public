package com.specure.response.sah.neutrality.result;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.response.LocationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.enums.NetNeutralityStatus;
import com.specure.response.sah.RadioSignalResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class NetNeutralityResultResponse {

    @JsonProperty("uuid")
    private final String uuid;

    @JsonProperty("openTestUuid")
    private final String openTestUuid;

    @JsonProperty("clientUuid")
    private final String clientUuid;

    @JsonProperty("type")
    private final NetNeutralityTestType type;

    @JsonProperty("durationNs")
    private final Long durationNs;

    @JsonProperty("testStatus")
    private final NetNeutralityStatus testStatus;

    @JsonProperty("measurementDate")
    private final String measurementDate;

    @JsonProperty("networkType")
    private final String networkType;

    @JsonProperty("networkName")
    private final String networkName;

    @JsonProperty("device")
    private final String device;

    @JsonProperty("location")
    private final LocationResponse location;

    @JsonProperty("measurementServerName")
    private final String measurementServerName;

    @JsonProperty("measurementServerCity")
    private final String measurementServerCity;

    @JsonProperty("operator")
    private final String operator;

    @JsonProperty(value = "radioSignals")
    private final List<RadioSignalResponse> radioSignals;
}
