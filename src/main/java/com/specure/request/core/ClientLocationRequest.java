package com.specure.request.core;

import com.specure.common.enums.MeasurementServerType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Data
@Getter
public class ClientLocationRequest {
    @NotNull
    private MeasurementServerType client;
    private String language;
    private LocationRequest location;
}
