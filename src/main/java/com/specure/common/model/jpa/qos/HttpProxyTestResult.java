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
public class HttpProxyTestResult extends QosResult {

    private String httpObjectiveUrl;
    private long httpResultDuration;
    private String httpResultHeader;
    private int httpResultLength;
    private String httpResultHash;
    private int httpResultStatus;
}
