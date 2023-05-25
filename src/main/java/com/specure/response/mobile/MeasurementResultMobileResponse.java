package com.specure.response.mobile;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MeasurementResultMobileResponse {

    private final List<String> error;
}
