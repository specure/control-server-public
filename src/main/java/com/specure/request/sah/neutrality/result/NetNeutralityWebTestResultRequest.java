package com.specure.request.sah.neutrality.result;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityWebTestResultRequest extends NetNeutralityTestResultRequest {
    public static final String TYPE = "WEB";

    @NotNull(message = "[timeoutExceeded] must not be null")
    private final Boolean timeoutExceeded;
    private final Long statusCode;

    public NetNeutralityWebTestResultRequest(@JsonProperty(value = "id") Long id,
                                             @JsonProperty(value = "openTestUuid") String openTestUuid,
                                             @JsonProperty(value = "clientUuid") String clientUuid,
                                             @JsonProperty(value = "durationNs") Long durationNs,
                                             @JsonProperty(value = "statusCode") Long statusCode,
                                             @JsonProperty(value = "timeoutExceeded") Boolean timeoutExceeded
    ) {
        super(NetNeutralityTestType.WEB, id, durationNs, openTestUuid, clientUuid);
        this.timeoutExceeded = timeoutExceeded;
        this.statusCode = statusCode;
    }
}
