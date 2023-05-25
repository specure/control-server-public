package com.specure.response.sah.neutrality.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class NetNeutralityOverallTestResultResponse {

    @JsonProperty(value = "testResults")
    private List<NetNeutralityResultResponse> netNeutralityResultResponse;
}
