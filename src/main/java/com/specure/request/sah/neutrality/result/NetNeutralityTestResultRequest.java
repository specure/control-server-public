package com.specure.request.sah.neutrality.result;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NetNeutralityDnsTestResultRequest.class, name = NetNeutralityDnsTestResultRequest.TYPE),
        @JsonSubTypes.Type(value = NetNeutralityWebTestResultRequest.class, name = NetNeutralityWebTestResultRequest.TYPE),
        @JsonSubTypes.Type(value = NetNeutralityUdpTestResultRequest.class, name = NetNeutralityUdpTestResultRequest.TYPE)
})
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public abstract class NetNeutralityTestResultRequest {

    @NotNull(message = "[type] must not be null")
    @JsonProperty(value = "type")
    private final NetNeutralityTestType type;

    @NotNull(message = "[id] must not be null")
    @JsonProperty(value = "id")
    private final Long id;

    @NotNull(message = "[durationNs] must not be null")
    @JsonProperty(value = "durationNs")
    private final Long durationNs;

    @NotNull(message = "[openTestUuid] must not be null")
    @JsonProperty(value = "openTestUuid")
    private final String openTestUuid;

    @NotNull(message = "[clientUuid] must not be null")
    @JsonProperty(value = "clientUuid")
    private final String clientUuid;
}
