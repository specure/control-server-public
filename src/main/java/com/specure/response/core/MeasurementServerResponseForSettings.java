package com.specure.response.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class MeasurementServerResponseForSettings {

    private final String name;

    private final String uuid;
}
