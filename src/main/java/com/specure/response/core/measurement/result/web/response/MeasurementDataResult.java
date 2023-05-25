package com.specure.response.core.measurement.result.web.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeasurementDataResult {
    private int classification;
    private String title;
    private String value;
}
