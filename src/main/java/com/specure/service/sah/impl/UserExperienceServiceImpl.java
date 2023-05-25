package com.specure.service.sah.impl;

import com.specure.common.constant.PropertyName;
import com.specure.common.enums.UserExperienceQuality;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.response.userexperience.RatingCondition;
import com.specure.common.response.userexperience.UserExperienceMetric;
import com.specure.common.response.userexperience.UserExperienceMetricsResponse;
import com.specure.common.response.userexperience.UserExperienceParameter;
import com.specure.common.utils.UnitForMeasurementParams;
import com.specure.common.utils.calculator.QualityCalculator;
import com.specure.multitenant.MultiTenantManager;
import com.specure.service.sah.UserExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserExperienceServiceImpl implements UserExperienceService {
    private final List<QualityCalculator> qualityCalculators;
    private final MultiTenantManager multiTenantManager;

    @Override
    public List<UserExperienceMetric> getBasicTestUserExperience(BasicTest basicTest) {
        final List<UserExperienceParameter> parameters = List.of(
                UserExperienceParameter.builder()
                        .field(PropertyName.DOWNLOAD_METRIC_PARAMETER)
                        .value(Optional.ofNullable(basicTest.getDownload())
                                .map(Integer::doubleValue)
                                .orElse(0.0))
                        .build(),
                UserExperienceParameter.builder()
                        .field(PropertyName.UPLOAD_METRIC_PARAMETER)
                        .value(Optional.ofNullable(basicTest.getUpload())
                                .map(Integer::doubleValue)
                                .orElse(0.0))
                        .build(),
                UserExperienceParameter.builder()
                        .field(PropertyName.PING_METRIC_PARAMETER)
                        .value(Optional.ofNullable(basicTest.getPing())
                                .map(Float::doubleValue)
                                .orElse(0.0))
                        .build()
        );
        return calculate(parameters);
    }

    private List<UserExperienceMetric> calculate(List<UserExperienceParameter> parameters) {
        log.trace("UserExperienceServiceImpl:calculate started with tenant = {}, parameters = {}", multiTenantManager.getCurrentTenant(), parameters);
        List<UserExperienceMetric> userExperienceMetrics = qualityCalculators.stream()
                .map(calculator -> {
                            UserExperienceQuality quality = calculator.calculate(parameters);
                            List<RatingCondition> ratingConditions = calculator.getRatingCondition(quality);
                            return UserExperienceMetric.builder()
                                    .category(calculator.getCategory())
                                    .quality(quality)
                                    .rating(ratingConditions)
                                    .build();
                        }
                ).collect(Collectors.toList());
        log.trace("UserExperienceServiceImpl:calculate finished with tenant = {}, userExperienceMetrics = {}", multiTenantManager.getCurrentTenant(), userExperienceMetrics);
        return userExperienceMetrics;
    }

    private UserExperienceMetricsResponse polishing(UserExperienceMetricsResponse userExperienceMetricsResponse) {
        log.trace("UserExperienceServiceImpl:polishing started with tenant = {}, userExperienceMetricsResponse = {}", multiTenantManager.getCurrentTenant(), userExperienceMetricsResponse);
        userExperienceMetricsResponse.setParameters(userExperienceMetricsResponse.getParameters()
                .stream()
                .filter(e -> !e.getField().equals(PropertyName.JITTER_METRIC_PARAMETER))
                .peek(e -> e.setValue(UnitForMeasurementParams.convertUnitValueOfMetric(e.getValue(), e.getField())))
                .collect(Collectors.toList()));
        log.trace("UserExperienceServiceImpl:polishing finished with tenant = {}, polishedUserExperienceMetricsResponse = {}", multiTenantManager.getCurrentTenant(), userExperienceMetricsResponse);
        return userExperienceMetricsResponse;
    }

}
