package com.specure.common.response.neutrality.crud;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsSettingResponse extends NetNeutralitySettingResponse {

    @JsonProperty(value = "target")
    private String target;

    @JsonProperty(value = "timeout")
    private Long timeout;

    @JsonProperty(value = "entryType")
    private String entryType;

    @JsonProperty(value = "resolver")
    private String resolver;

    @JsonProperty(value = "expectedDnsStatus")
    private String expectedDnsStatus;

    @JsonProperty(value = "expectedDnsEntries")
    private String expectedDnsEntries;

    public NetNeutralityDnsSettingResponse(Long id,
                                           boolean isActive,
                                           String target,
                                           Long timeout,
                                           String entryType,
                                           String resolver,
                                           String expectedDnsStatus,
                                           String expectedDnsEntries) {
        super(id, NetNeutralityTestType.DNS, isActive);
        this.target = target;
        this.timeout = timeout;
        this.entryType = entryType;
        this.resolver = resolver;
        this.expectedDnsStatus = expectedDnsStatus;
        this.expectedDnsEntries = expectedDnsEntries;
    }
}
