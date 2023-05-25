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
@JsonTypeName("udp")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UdpTestResultRequest extends TestResult {
    private int udpResultOutNumPackets;
    private int udpResultOutResponseNumPackets;
    private int udpObjectiveOutNumPackets;
    private String udpResultOutPacketLossRate;
    private int udpObjectiveOutPort;
    private long udpObjectiveDelay;
    private long udpObjectiveTimeout;
}
