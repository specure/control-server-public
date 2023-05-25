package com.specure.response.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class MobileMeasurementResultContainerResponse {

    @JsonProperty(value = "testresult")
    private final List<MobileMeasurementResultResponse> mobileMeasurementResultRespons;
}
