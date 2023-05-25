package com.specure.response.sah.neutrality.parameters;

import com.specure.common.enums.NetNeutralityTestType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityUdpTestParametersResponse extends NetNeutralityTestParameterResponse {

    private final Long timeout;
    private final Long portNumber;
    private final Long numberOfPacketsSent;
    private final Long minNumberOfPacketsReceived;

    public NetNeutralityUdpTestParametersResponse(Long id,
                                                  Long timeout,
                                                  Long portNumber,
                                                  Long numberOfPacketsSent,
                                                  Long minNumberOfPacketsReceived) {
        super(id, NetNeutralityTestType.UDP);
        this.timeout = timeout;
        this.portNumber = portNumber;
        this.numberOfPacketsSent = numberOfPacketsSent;
        this.minNumberOfPacketsReceived = minNumberOfPacketsReceived;
    }
}
