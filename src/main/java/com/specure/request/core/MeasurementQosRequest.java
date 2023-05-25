package com.specure.request.core;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.constant.ErrorMessage;
import com.specure.request.core.measurement.qos.request.TestResult;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementQosRequest {
    @NotNull(message = ErrorMessage.TEST_TOKEN_REQUIRED)
    private String testToken;
    private String clientUuid;
    private long time;
    private String clientVersion;
    @ToString.Exclude
    private List<TestResult> qosResult;
    private String clientName;
    private String clientLanguage;
}

