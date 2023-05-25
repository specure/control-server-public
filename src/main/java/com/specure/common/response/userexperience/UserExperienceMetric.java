package com.specure.common.response.userexperience;

import com.specure.common.enums.UserExperienceCategory;
import com.specure.common.enums.UserExperienceQuality;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserExperienceMetric {
    private UserExperienceQuality quality;
    private UserExperienceCategory category;
    private List<RatingCondition> rating;
}
