package com.specure.response.sah.neutrality.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.NetNeutralityTestType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsTestParametersResponse extends NetNeutralityTestParameterResponse {

    @JsonProperty(value = "target")
    private final String target;

    @JsonProperty(value = "timeout")
    private final Long timeout;

    @JsonProperty(value = "entryType")
    private String entryType;

    @JsonProperty(value = "resolver")
    private final String resolver;

    public NetNeutralityDnsTestParametersResponse(Long id,
                                                  String target,
                                                  Long timeout,
                                                  String entryType,
                                                  String resolver) {
        super(id, NetNeutralityTestType.DNS);
        this.target = target;
        this.timeout = timeout;
        this.entryType = entryType;
        this.resolver = resolver;
    }
}
