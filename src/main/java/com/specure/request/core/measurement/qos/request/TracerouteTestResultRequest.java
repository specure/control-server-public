package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@JsonTypeName("traceroute")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TracerouteTestResultRequest extends TestResult {

    private String tracerouteObjectiveHost;

    private String tracerouteResultStatus;

    private Long tracerouteResultDuration;

    private Long tracerouteObjectiveTimeout;

    private Integer tracerouteObjectiveMaxHops;

    private Integer tracerouteResultHops;

    private List<PathElementEntriesRequest> tracerouteResultDetails;
}
