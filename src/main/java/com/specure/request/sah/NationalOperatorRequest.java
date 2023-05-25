package com.specure.request.sah;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class NationalOperatorRequest {

    private final String name;

    private final String alias;
}
