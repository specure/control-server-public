package com.specure.response.core.measurement.qos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UdpSettingsResponse {
    private String concurrencyGroup;
    private String serverPort;
    private String qosTestUid;
    private String serverAddr;
    private String outNumPackets;
    private String timeout;
    private String outPort;
}