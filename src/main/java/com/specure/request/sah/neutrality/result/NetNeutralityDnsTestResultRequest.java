package com.specure.request.sah.neutrality.result;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsTestResultRequest extends NetNeutralityTestResultRequest {
    public static final String TYPE = "DNS";

    @NotNull(message = "[timeoutExceeded] must not be null")
    private final Boolean timeoutExceeded;

    private final List<@NotBlank(message = "Dns entries must not be empty string") String> dnsEntries;

    @NotNull(message = "[dnsStatus] must not be null")
    @ApiModelProperty(notes = "status of the result [\"NOERROR\", \"NXDOMAIN\"]", example = "NXDOMAIN")
    private final String dnsStatus;

    @NotNull(message = "[resolver] must not be null")
    private final String resolver;

    @JsonCreator
    public NetNeutralityDnsTestResultRequest(@JsonProperty(value = "id") Long id,
                                             @JsonProperty(value = "openTestUuid") String openTestUuid,
                                             @JsonProperty(value = "clientUuid") String clientUuid,
                                             @JsonProperty(value = "durationNs") Long durationNs,
                                             @JsonProperty(value = "timeoutExceeded") Boolean timeoutExceeded,
                                             @JsonProperty(value = "dnsStatus") String dnsStatus,
                                             @JsonProperty(value = "dnsEntries") List<String> dnsEntries,
                                             @JsonProperty(value = "resolver") String resolver) {
        super(NetNeutralityTestType.DNS, id, durationNs, openTestUuid, clientUuid);
        this.timeoutExceeded = timeoutExceeded;
        this.dnsEntries = dnsEntries;
        this.dnsStatus = dnsStatus;
        this.resolver = resolver;
    }
}
