package com.specure.response.sah.neutrality.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.NetNeutralityTestType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class NetNeutralityParametersResponse {

    @JsonProperty("netNeutralityParameters")
    Map<NetNeutralityTestType, List<NetNeutralityTestParameterResponse>> netNeutralityParameters;
}
