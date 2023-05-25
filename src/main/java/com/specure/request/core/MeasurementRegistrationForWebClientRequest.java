package com.specure.request.core;

import com.specure.common.enums.MeasurementServerType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class MeasurementRegistrationForWebClientRequest {

    @NotNull
    private MeasurementServerType client;

    private String language;
    private long time;
    private String timezone;
    private String type;

    private String uuid;

    private String version;
    private String version_code;
}
