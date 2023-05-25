package com.specure.common.utils.calculator;

import com.specure.common.enums.UserExperienceCategory;
import com.specure.common.enums.UserExperienceQuality;
import com.specure.common.response.userexperience.RatingCondition;
import com.specure.common.response.userexperience.UserExperienceParameter;

import java.util.List;

public interface QualityCalculator {
    UserExperienceCategory getCategory();

    UserExperienceQuality calculate(List<UserExperienceParameter> parameters);

    List<RatingCondition> getRatingCondition(UserExperienceQuality quality);
}
