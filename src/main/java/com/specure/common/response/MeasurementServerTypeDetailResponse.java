package com.specure.common.response;

import com.specure.common.enums.MeasurementServerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeasurementServerTypeDetailResponse {

    @JsonProperty(value = "serverType")
    private final MeasurementServerType serverType;

    @JsonProperty(value = "port")
    private final Long port;

    @JsonProperty(value = "portSsl")
    private final Long portSsl;

    @JsonProperty(value = "encrypted")
    private final boolean encrypted;
}
