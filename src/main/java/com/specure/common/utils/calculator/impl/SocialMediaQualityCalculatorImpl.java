package com.specure.common.utils.calculator.impl;

import com.specure.common.constant.PropertyName;
import com.specure.common.enums.UserExperienceCategory;
import com.specure.common.enums.UserExperienceQuality;
import com.specure.common.model.dto.ParameterCondition;
import com.specure.common.response.userexperience.UserExperienceParameter;
import com.specure.common.utils.calculator.BaseQualityCalculator;
import com.specure.common.utils.calculator.QualityCalculator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.specure.common.enums.Condition.LESS;
import static com.specure.common.enums.Condition.MORE_OR_EQUAL;

@Component
public class SocialMediaQualityCalculatorImpl extends BaseQualityCalculator implements QualityCalculator {
    public SocialMediaQualityCalculatorImpl() {
        ratingConditionMap.put(UserExperienceQuality.EXCELLENT,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 10000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 5000)
                ));
        ratingConditionMap.put(UserExperienceQuality.GOOD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 5000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 3000)
                ));
        ratingConditionMap.put(UserExperienceQuality.MODERATE,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 1500),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 1500)
                ));
        ratingConditionMap.put(UserExperienceQuality.POOR,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 500),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 1000)
                ));
        ratingConditionMap.put(UserExperienceQuality.BAD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 500),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 1000)
                ));
    }

    @Override
    public UserExperienceCategory getCategory() {
        return UserExperienceCategory.SOCIAL_MEDIA;
    }

    @Override
    public UserExperienceQuality calculate(List<UserExperienceParameter> parameters) {
        ArrayList<UserExperienceQuality> socialMediaMarks = new ArrayList<>();
        socialMediaMarks.add(getQuality(parameters, PropertyName.DOWNLOAD_METRIC_PARAMETER));
        socialMediaMarks.add(getQuality(parameters, PropertyName.UPLOAD_METRIC_PARAMETER));
        return getWorst(socialMediaMarks);
    }
}
