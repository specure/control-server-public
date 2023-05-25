package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeasurementSettingsResponse {

    @JsonProperty(value = "measurementDuration")
    private final String measurementDuration;

    @JsonProperty(value = "measurementNumThreads")
    private final String measurementNumThreads;

    @JsonProperty(value = "measurementNumPings")
    private final String measurementNumPings;
}
