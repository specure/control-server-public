package com.specure.request.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.request.core.CapabilitiesRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MobileMeasurementResultRequest {

    @JsonProperty(value = "language")
    private final String language;

    @JsonProperty(value = "test_uuid")
    private final UUID testUUID;

    @JsonProperty(value = "capabilities")
    private final CapabilitiesRequest capabilitiesRequest;
}
