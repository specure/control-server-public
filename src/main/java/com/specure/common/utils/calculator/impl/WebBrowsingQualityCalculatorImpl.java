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
public class WebBrowsingQualityCalculatorImpl extends BaseQualityCalculator implements QualityCalculator {
    public WebBrowsingQualityCalculatorImpl() {
        ratingConditionMap.put(UserExperienceQuality.EXCELLENT,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 10000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 500),
                        PropertyName.PING_METRIC_PARAMETER, new ParameterCondition(LESS, 50)
                ));
        ratingConditionMap.put(UserExperienceQuality.GOOD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 5000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 300),
                        PropertyName.PING_METRIC_PARAMETER, new ParameterCondition(LESS, 100)
                ));
        ratingConditionMap.put(UserExperienceQuality.MODERATE,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 3000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 100),
                        PropertyName.PING_METRIC_PARAMETER, new ParameterCondition(LESS, 150)
                ));
        ratingConditionMap.put(UserExperienceQuality.POOR,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 1000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 50),
                        PropertyName.PING_METRIC_PARAMETER, new ParameterCondition(LESS, 200)
                ));
        ratingConditionMap.put(UserExperienceQuality.BAD,
                Map.of(
                        PropertyName.DOWNLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 1000),
                        PropertyName.UPLOAD_METRIC_PARAMETER, new ParameterCondition(LESS, 50),
                        PropertyName.PING_METRIC_PARAMETER, new ParameterCondition(MORE_OR_EQUAL, 200)
                ));
    }

    @Override
    public UserExperienceCategory getCategory() {
        return UserExperienceCategory.WEB_BROWSING;
    }

    @Override
    public UserExperienceQuality calculate(List<UserExperienceParameter> parameters) {
        ArrayList<UserExperienceQuality> webBrowsingMarks = new ArrayList<>();
        webBrowsingMarks.add(getQuality(parameters, PropertyName.DOWNLOAD_METRIC_PARAMETER));
        webBrowsingMarks.add(getQuality(parameters, PropertyName.UPLOAD_METRIC_PARAMETER));
        webBrowsingMarks.add(getQuality(parameters, PropertyName.PING_METRIC_PARAMETER));
        return getWorst(webBrowsingMarks);
    }
}
