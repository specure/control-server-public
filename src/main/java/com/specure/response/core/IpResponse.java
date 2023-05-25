package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class IpResponse {

    @JsonProperty(value = "v")
    private final String version;

    @JsonProperty(value = "ip")
    private final String ip;
}
