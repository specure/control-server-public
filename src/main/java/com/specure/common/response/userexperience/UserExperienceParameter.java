package com.specure.common.response.userexperience;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserExperienceParameter {
    private String field;
    private Double value;
    private String unit;
    private int order;
}
