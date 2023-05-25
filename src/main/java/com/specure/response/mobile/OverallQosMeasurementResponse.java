package com.specure.response.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.model.dto.TestResultCounter;
import com.specure.response.sah.ErrorContainerResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OverallQosMeasurementResponse extends ErrorContainerResponse {

    @JsonProperty(value = "overallQos")
    private Float overallQos;

    @JsonProperty(value = "qosTestResultCounters")
    List<TestResultCounter> qosTestResultCounters;
}
