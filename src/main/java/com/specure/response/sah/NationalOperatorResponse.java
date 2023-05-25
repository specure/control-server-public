package com.specure.response.sah;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class NationalOperatorResponse {

    private final Long id;

    private final String name;

    private final String alias;
}
