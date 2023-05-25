package com.specure.common.utils.calculator.impl;

import com.specure.common.constant.PropertyName;
import com.specure.common.enums.UserExperienceCategory;
import com.specure.common.enums.UserExperienceQuality;
import com.specure.common.model.dto.ParameterCondition;
import com.specure.common.response.userexperience.UserExperienceParameter;
import com.specure.common.utils.calculator.BaseQualityCalculator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.specure.common.enums.Condition.LESS;
import static com.specure.common.enums.Condition.MORE_OR_EQUAL;

@Component
public class EmailMessagingQualityCalculatorImpl extends BaseQualityCalculator {

    public EmailMessagingQualityCalculatorImpl() {
        ratingConditionMap.put(UserExperienceQuality.EXCELLENT,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 3000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 500)
                ));
        ratingConditionMap.put(UserExperienceQuality.GOOD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 1000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 300)
                ));
        ratingConditionMap.put(UserExperienceQuality.MODERATE,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 500),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 100)
                ));
        ratingConditionMap.put(UserExperienceQuality.POOR,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 100),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 50)
                ));
        ratingConditionMap.put(UserExperienceQuality.BAD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 100),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 50)
                ));
    }

    @Override
    public UserExperienceCategory getCategory() {
        return UserExperienceCategory.EMAIL_MESSAGING;
    }

    @Override
    final public UserExperienceQuality calculate(List<UserExperienceParameter> parameters) {
        ArrayList<UserExperienceQuality> emailMarks = new ArrayList<>();
        emailMarks.add(getQuality(parameters, PropertyName.DOWNLOAD_METRIC_PARAMETER));
        emailMarks.add(getQuality(parameters, PropertyName.UPLOAD_METRIC_PARAMETER));
        return getWorst(emailMarks);
    }
}
