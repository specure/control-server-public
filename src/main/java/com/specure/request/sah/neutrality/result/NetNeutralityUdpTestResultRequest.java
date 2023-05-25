package com.specure.request.sah.neutrality.result;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityUdpTestResultRequest extends NetNeutralityTestResultRequest {
    public static final String TYPE = "UDP";

    @NotNull(message = "[numberOfPacketsReceived] must not be null")
    private final Long numberOfPacketsReceived;


    @JsonCreator
    public NetNeutralityUdpTestResultRequest(@JsonProperty(value = "id") Long id,
                                             @JsonProperty(value = "openTestUuid") String openTestUuid,
                                             @JsonProperty(value = "clientUuid") String clientUuid,
                                             @JsonProperty(value = "durationNs") Long durationNs,
                                             @JsonProperty(value = "numberOfPacketsReceived") Long numberOfPacketsReceived) {
        super(NetNeutralityTestType.UDP, id, durationNs, openTestUuid, clientUuid);
        this.numberOfPacketsReceived = numberOfPacketsReceived;
    }
}
