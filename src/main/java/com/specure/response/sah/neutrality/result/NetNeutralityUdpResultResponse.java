package com.specure.response.sah.neutrality.result;


import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.response.LocationResponse;
import com.specure.enums.NetNeutralityStatus;
import com.specure.response.sah.RadioSignalResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NetNeutralityUdpResultResponse extends NetNeutralityResultResponse {

    private final Long portNumber;
    private final Long numberOfPacketsSent;
    private final Long actualNumberOfPacketsReceived;
    private final Long expectedNumberOfPacketsReceived;

    public NetNeutralityUdpResultResponse(String uuid,
                                          String openTestUuid,
                                          String clientUuid,
                                          Long durationNs,
                                          NetNeutralityStatus success,
                                          String measurementDate,
                                          String networkType,
                                          String networkName,
                                          String device,
                                          LocationResponse location,
                                          String measurementServerName,
                                          String measurementServerNameCity,
                                          String operator,
                                          List<RadioSignalResponse> radioSignals,
                                          Long portNumber,
                                          Long numberOfPacketsSent,
                                          Long actualNumberOfPacketsReceived,
                                          Long expectedNumberOfPacketsReceived) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.UDP,
                durationNs,
                success,
                measurementDate,
                networkType,
                networkName,
                device,
                location,
                measurementServerName,
                measurementServerNameCity,
                operator,
                radioSignals);
        this.portNumber = portNumber;
        this.numberOfPacketsSent = numberOfPacketsSent;
        this.actualNumberOfPacketsReceived = actualNumberOfPacketsReceived;
        this.expectedNumberOfPacketsReceived = expectedNumberOfPacketsReceived;
    }
}
