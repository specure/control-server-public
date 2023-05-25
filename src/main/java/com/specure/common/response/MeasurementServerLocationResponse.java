package com.specure.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeasurementServerLocationResponse {

    private Double latitude;

    private Double longitude;
}
