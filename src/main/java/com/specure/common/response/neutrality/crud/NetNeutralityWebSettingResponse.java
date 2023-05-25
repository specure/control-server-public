package com.specure.common.response.neutrality.crud;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityWebSettingResponse extends NetNeutralitySettingResponse {

    @JsonProperty(value = "target")
    private String target;

    @JsonProperty(value = "timeout")
    private Long timeout;

    @JsonProperty(value = "expectedStatusCode")
    private Long expectedStatusCode;

    public NetNeutralityWebSettingResponse(Long id,
                                           boolean isActive,
                                           String target,
                                           Long timeout,
                                           Long expectedStatusCode) {
        super(id, NetNeutralityTestType.WEB, isActive);
        this.target = target;
        this.timeout = timeout;
        this.expectedStatusCode = expectedStatusCode;
    }
}
