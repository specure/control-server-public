package com.specure.request.core;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeasurementQosParametersRequest {
    private String uuid;
}
