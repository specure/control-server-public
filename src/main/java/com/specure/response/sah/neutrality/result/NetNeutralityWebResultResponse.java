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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityWebResultResponse extends NetNeutralityResultResponse {

    private final String url;
    private final Long timeout;
    private final Long actualStatusCode;
    private final Long expectedStatusCode;
    private final boolean timeoutExceeded;

    public NetNeutralityWebResultResponse(String uuid,
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
                                          String url,
                                          Long timeout,
                                          Long actualStatusCode,
                                          Long expectedStatusCode,
                                          boolean timeoutExceeded) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.WEB,
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
        this.url = url;
        this.timeout = timeout;
        this.actualStatusCode = actualStatusCode;
        this.expectedStatusCode = expectedStatusCode;
        this.timeoutExceeded = timeoutExceeded;
    }
}
