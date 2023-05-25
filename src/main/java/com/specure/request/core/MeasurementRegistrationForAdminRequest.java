package com.specure.request.core;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.Platform;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementRegistrationForAdminRequest {

    @NotNull(message = "Measurement server id is required")
    private Long measurementServerId;

    @NotNull(message = "Client type is required")
    private MeasurementServerType client;

    private String uuid;

    private Boolean telephonyPermissionGranted;

    private Boolean locationPermissionGranted;

    private Boolean uuidPermissionGranted;
    private String appVersion;
    private Platform platform;
}
