package com.specure.common.model.jpa.qos;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebsiteTestResult extends QosResult {

    private String websiteResultInfo;

    private String websiteObjectiveUrl;

    private String websiteResultStatus;

    private Long websiteResultDuration;

    private Long websiteResultRxBytes;

    private Long websiteResultTxBytes;

    private Long websiteObjectiveTimeout;
}
