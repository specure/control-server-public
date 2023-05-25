package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MeasurementResultRMBTClientResponse {
    @JsonProperty("RMBT_RESULT_URL")
    private String rmbtResultUrl;
    private List<String> error;
}
