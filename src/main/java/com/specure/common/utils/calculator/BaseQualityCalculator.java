package com.specure.common.utils.calculator;

import com.specure.common.constant.Constants;
import com.specure.common.constant.PropertyName;
import com.specure.common.enums.UserExperienceQuality;
import com.specure.common.exception.WrongNameOfUserExperienceParameterException;
import com.specure.common.model.dto.ParameterCondition;
import com.specure.common.model.dto.QualityComparator;
import com.specure.common.response.userexperience.RatingCondition;
import com.specure.common.response.userexperience.UserExperienceParameter;
import com.specure.common.utils.UnitForMeasurementParams;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseQualityCalculator implements QualityCalculator {
    protected final Map<UserExperienceQuality, Map<String, ParameterCondition>> ratingConditionMap = new EnumMap<>(UserExperienceQuality.class);

    @Override
    public List<RatingCondition> getRatingCondition(UserExperienceQuality quality) {
        Map<String, ParameterCondition> ratingConditionMapForQuality = ratingConditionMap.get(quality);
        List<RatingCondition> ratingConditions = new ArrayList<>();
        ratingConditions.add(buildRatingCondition(PropertyName.DOWNLOAD_METRIC_PARAMETER, ratingConditionMapForQuality));
        ratingConditions.add(buildRatingCondition(PropertyName.UPLOAD_METRIC_PARAMETER, ratingConditionMapForQuality));
        ratingConditions.add(buildRatingCondition(PropertyName.PING_METRIC_PARAMETER, ratingConditionMapForQuality));
        return ratingConditions.stream()
                .sorted(Comparator.comparing(RatingCondition::getMetric))
                .collect(Collectors.toList());
    }

    protected UserExperienceQuality getQuality(List<UserExperienceParameter> parameters, String metricName) {
        return calculateQuality(
                getValueByName(metricName, parameters),
                ratingConditionMap.get(UserExperienceQuality.EXCELLENT).get(metricName),
                ratingConditionMap.get(UserExperienceQuality.GOOD).get(metricName),
                ratingConditionMap.get(UserExperienceQuality.MODERATE).get(metricName),
                ratingConditionMap.get(UserExperienceQuality.POOR).get(metricName)
        );
    }

    protected UserExperienceQuality calculateQuality(double value, ParameterCondition excellent, ParameterCondition good, ParameterCondition moderate, ParameterCondition poor) {

        if (excellent.isTrue(value)) return UserExperienceQuality.EXCELLENT;
        if (good.isTrue(value)) return UserExperienceQuality.GOOD;
        if (moderate.isTrue(value)) return UserExperienceQuality.MODERATE;
        if (poor.isTrue(value)) return UserExperienceQuality.POOR;
        return UserExperienceQuality.BAD;
    }

    protected double getValueByName(String name, List<UserExperienceParameter> parameters) {
        return parameters
                .stream()
                .filter(e -> e.getField().equals(name))
                .findFirst()
                .orElseThrow(() -> new WrongNameOfUserExperienceParameterException(name))
                .getValue();
    }

    protected UserExperienceQuality getWorst(List<UserExperienceQuality> allQualityMarks) {
        allQualityMarks.sort(new QualityComparator());
        return allQualityMarks.get(0);
    }

    private RatingCondition buildRatingCondition(String metricName, Map<String, ParameterCondition> ratingConditionMapForQuality) {
        return Optional.ofNullable(ratingConditionMapForQuality.get(metricName))
                .map(e -> RatingCondition.builder()
                        .metric(metricName)
                        .condition(e.getCondition().getValue())
                        .value(UnitForMeasurementParams.convertUnitValueOfMetric(e.getValue(), metricName))
                        .unit(UnitForMeasurementParams.getUnitByAggregationName(metricName))
                        .build())
                .orElse(RatingCondition.builder()
                        .metric(metricName)
                        .condition(Constants.USER_EXPERIENCE_NOT_APPLICABLE_METRIC)
                        .build());
    }
}
