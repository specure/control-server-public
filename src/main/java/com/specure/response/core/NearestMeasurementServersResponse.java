package com.specure.response.core;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NearestMeasurementServersResponse {
    private List<String> error;
    private List<MeasurementServerForWebResponse> servers;
}
