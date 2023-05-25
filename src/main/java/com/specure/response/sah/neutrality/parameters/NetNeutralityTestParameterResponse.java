package com.specure.response.sah.neutrality.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.NetNeutralityTestType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class NetNeutralityTestParameterResponse {

    @JsonProperty(value = "id")
    private final Long id;

    @JsonProperty(value = "type")
    private final NetNeutralityTestType type;
}
