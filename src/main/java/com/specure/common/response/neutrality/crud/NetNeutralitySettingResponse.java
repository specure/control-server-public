package com.specure.common.response.neutrality.crud;

import com.specure.common.enums.NetNeutralityTestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class NetNeutralitySettingResponse {

    @JsonProperty(value = "id")
    private final Long id;

    @JsonProperty(value = "type")
    private final NetNeutralityTestType type;

    @JsonProperty(value = "isActive")
    private final boolean isActive;
}
