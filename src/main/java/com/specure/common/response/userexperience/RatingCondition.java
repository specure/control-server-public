package com.specure.common.response.userexperience;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class RatingCondition {

    private final String metric;

    private final String condition;

    private final Double value;

    private final String unit;
}
