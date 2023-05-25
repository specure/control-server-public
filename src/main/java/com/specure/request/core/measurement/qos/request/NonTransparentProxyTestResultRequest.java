package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonTypeName("non_transparent_proxy")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NonTransparentProxyTestResultRequest extends TestResult {

    @JsonProperty("nontransproxy_result")
    private String nonTransparentProxyResult;

    @JsonProperty("nontransproxy_objective_request")
    private String nonTransparentProxyObjectiveRequest;

    @JsonProperty("nontransproxy_objective_timeout")
    private long nonTransparentProxyObjectiveTimeout;

    @JsonProperty("nontransproxy_objective_port")
    private int nonTransparentProxyObjectivePort;

    @JsonProperty("nontransproxy_result_response")
    private String nonTransparentProxyResultResponse;
}
