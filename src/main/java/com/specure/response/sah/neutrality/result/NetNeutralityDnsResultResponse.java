package com.specure.response.sah.neutrality.result;


import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.response.LocationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.enums.NetNeutralityStatus;
import com.specure.response.sah.RadioSignalResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsResultResponse extends NetNeutralityResultResponse {

    @JsonProperty("target")
    private final String target;
    @JsonProperty("actualResolver")
    private final String actualResolver;
    @JsonProperty("expectedResolver")
    private final String expectedResolver;
    @JsonProperty("timeout")
    private final Long timeout;
    @JsonProperty("actualDnsStatus")
    private final String actualDnsStatus;
    @JsonProperty("expectedDnsStatus")
    private final String expectedDnsStatus;
    @JsonProperty("entryType")
    private final String entryType;
    @JsonProperty("actualDnsResultEntriesFound")
    private final String actualDnsResultEntries;
    @JsonProperty("expectedDnsResultEntriesFound")
    private final String expectedDnsResultEntries;
    @JsonProperty("timeoutExceeded")
    private final boolean timeoutExceeded;
    @JsonProperty("failReason")
    private final String failReason;

    public NetNeutralityDnsResultResponse(String uuid,
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
                                          String host,
                                          String actualResolver,
                                          String expectedResolver,
                                          Long timeout,
                                          String actualDnsStatus,
                                          String expectedDnsStatus,
                                          String entryType,
                                          String actualDnsResultEntries,
                                          String expectedDnsResultEntries,
                                          boolean timeoutExceeded,
                                          String failReason) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.DNS,
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
        this.target = host;
        this.actualResolver = actualResolver;
        this.expectedResolver = expectedResolver;
        this.timeout = timeout;
        this.actualDnsStatus = actualDnsStatus;
        this.expectedDnsStatus = expectedDnsStatus;
        this.entryType = entryType;
        this.timeoutExceeded = timeoutExceeded;
        this.actualDnsResultEntries = actualDnsResultEntries;
        this.expectedDnsResultEntries = expectedDnsResultEntries;
        this.failReason = failReason;
    }
}
