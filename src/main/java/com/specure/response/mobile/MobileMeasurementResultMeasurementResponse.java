package com.specure.response.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MobileMeasurementResultMeasurementResponse {

    @JsonProperty(value = "value")
    private final String value;

    @JsonProperty(value = "title")
    private final String title;

    @JsonProperty(value = "classification")
    private final Integer classification;
}
