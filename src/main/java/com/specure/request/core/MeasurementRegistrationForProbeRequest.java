package com.specure.request.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.constant.ErrorMessage;
import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.Platform;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class MeasurementRegistrationForProbeRequest {

    @NotNull
    private MeasurementServerType client;

    @NotNull(message = ErrorMessage.PORT_NAME_REQUIRED)
    @JsonProperty("ifport")
    private String port;

    @NotBlank(message = ErrorMessage.CLIENT_UUID_REQUIRED)
    private String uuid;

    @JsonProperty("on-net")
    private boolean isOnNet;
    @JsonProperty("app_version")
    private String appVersion;
    @JsonProperty("platform")
    private Platform platform;
}
