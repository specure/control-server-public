package com.specure.response.sah.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NationalTableResponse {
    @JsonProperty("statsByProvider")
    List<StatsByOperatorResponse> statsByOperator;
    Double averageUpload;
    Double averageDownload;
    Double averageLatency;
    Long allMeasurements;
}
