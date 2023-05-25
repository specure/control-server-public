package com.specure.request.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CapabilitiesRequest {

    private final ClassificationRequest classification;

    private final QosRequest qos;

    @JsonProperty(value = "RMBThttp")
    private final boolean rmbtHttp;
}
