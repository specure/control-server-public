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
public class UdpTestResult extends QosResult {

    private int udpResultOutNumPackets;
    private int udpResultOutResponseNumPackets;
    private int udpObjectiveOutNumPackets;
    private String udpResultOutPacketLossRate;
    private int udpObjectiveOutPort;
    private long udpObjectiveDelay;
    private long udpObjectiveTimeout;
}
