package com.specure.common.response.neutrality.crud;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityUdpSettingResponse extends NetNeutralitySettingResponse {

    @JsonProperty(value = "timeout")
    private Long timeout;

    @JsonProperty(value = "portNumber")
    private Long portNumber;

    @JsonProperty(value = "numberOfPacketsSent")
    private Long numberOfPacketsSent;

    @JsonProperty(value = "minNumberOfPacketsReceived")
    private Long minNumberOfPacketsReceived;

    public NetNeutralityUdpSettingResponse(Long id,
                                           boolean isActive,
                                           Long timeout,
                                           Long portNumber,
                                           Long numberOfPacketsSent,
                                           Long minNumberOfPacketsReceived) {
        super(id, NetNeutralityTestType.UDP, isActive);
        this.timeout = timeout;
        this.portNumber = portNumber;
        this.numberOfPacketsSent = numberOfPacketsSent;
        this.minNumberOfPacketsReceived = minNumberOfPacketsReceived;
    }
}
