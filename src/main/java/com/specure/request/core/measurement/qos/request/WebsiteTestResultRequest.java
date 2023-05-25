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
@JsonTypeName("website")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteTestResultRequest extends TestResult {

    private String websiteResultInfo;

    private String websiteObjectiveUrl;

    private String websiteResultStatus;

    private Long websiteResultDuration;

    private Long websiteResultRxBytes;

    private Long websiteResultTxBytes;

    private Long websiteObjectiveTimeout;
}
