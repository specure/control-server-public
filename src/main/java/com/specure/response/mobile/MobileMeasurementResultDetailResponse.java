package com.specure.response.mobile;

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
public class MobileMeasurementResultDetailResponse {

    @JsonProperty(value = "testresultdetail")
    private final List<MobileMeasurementResultDetailContainerResponse> mobileMeasurementResultDetailContainerResponse;
}
