package com.specure.response.core.measurement.result.Detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MeasurementDetailResultResponse {
    private List<String> error;

    @JsonProperty("testresultdetail")
    private List<MeasurementDetailDataFragment> measurementResultDetail;
}
