package com.specure.request.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class MobileMeasurementResultDetailRequest {

    @JsonProperty(value = "test_uuid")
    private final UUID testUUID;

    @JsonProperty(value = "language")
    private final String language;
}
