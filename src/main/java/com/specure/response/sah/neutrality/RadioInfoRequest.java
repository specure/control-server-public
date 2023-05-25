package com.specure.response.sah.neutrality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.request.core.measurement.request.RadioSignalRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class RadioInfoRequest {

    @JsonProperty(value = "signals")
    private final List<RadioSignalRequest> signals;
}
