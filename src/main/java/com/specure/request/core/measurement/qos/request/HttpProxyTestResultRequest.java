package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("http_proxy")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpProxyTestResultRequest extends TestResult {
    private String httpObjectiveUrl;
    private long httpResultDuration;
    private String httpResultHeader;
    private int httpResultLength;
    private String httpResultHash;
    private int httpResultStatus;
}
