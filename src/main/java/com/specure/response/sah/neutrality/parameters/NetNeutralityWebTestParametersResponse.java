package com.specure.response.sah.neutrality.parameters;

import com.specure.common.enums.NetNeutralityTestType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityWebTestParametersResponse extends NetNeutralityTestParameterResponse {

    private final String target;

    private final Long timeout;

    public NetNeutralityWebTestParametersResponse(Long id,
                                                  String target,
                                                  Long timeout) {
        super(id, NetNeutralityTestType.WEB);
        this.target = target;
        this.timeout = timeout;
    }
}
