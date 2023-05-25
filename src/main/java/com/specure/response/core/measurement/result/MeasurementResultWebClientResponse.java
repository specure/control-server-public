package com.specure.response.core.measurement.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.response.core.measurement.result.web.response.MeasurementTestResultForWeb;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MeasurementResultWebClientResponse {
    private List<String> error;
    @JsonProperty("testresult")
    private List<MeasurementTestResultForWeb> testResult;
}
