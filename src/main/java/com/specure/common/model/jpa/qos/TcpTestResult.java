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
public class TcpTestResult extends QosResult {

    private String tcpResultOut;
    private Long tcpObjectiveTimeout;
    private String tcpResultOutResponse;
    private int tcpObjectiveOutPort;
}
