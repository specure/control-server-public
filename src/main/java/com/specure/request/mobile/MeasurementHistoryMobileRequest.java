package com.specure.request.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MeasurementHistoryMobileRequest {

    @JsonProperty(value = "uuid")
    private final String clientUuid;

    @JsonProperty(value = "devices")
    private final List<String> devices;

    @JsonProperty(value = "network_types")
    private final List<String> networkTypes;
}
