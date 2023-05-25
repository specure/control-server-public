package com.specure.response.sah.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsByOperatorResponse {
    @JsonProperty("providerName")
    String operatorName;
    Double upload;
    Double download;
    Double latency;
    Long measurements;
}
