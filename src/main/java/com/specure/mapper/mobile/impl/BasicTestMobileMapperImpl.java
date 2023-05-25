package com.specure.mapper.mobile.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.service.sah.UserExperienceService;
import com.specure.utils.mobile.QosUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicTestMobileMapperImpl implements BasicTestMobileMapper {
    private final UserExperienceService userExperienceService;

    @Override
    public BasicTestHistoryMobileResponse basicTestResponseToBasicTestHistoryMobileResponse(BasicTest basicTest, BasicQosTest basicQosTest) {
        return BasicTestHistoryMobileResponse.builder()
                .openTestUuid(basicTest.getOpenTestUuid())
                .downloadSpeed(basicTest.getDownload())
                .uploadSpeed(basicTest.getUpload())
                .ping(basicTest.getPing())
                .jitter(basicTest.getJitter())
                .packetLoss(basicTest.getPacketLoss())
                .networkType(BasicTestMobileMapper.getNetworkType(basicTest))
                .qos(Optional.ofNullable(basicQosTest)
                        .map(BasicQosTest::getOverallQos).map(QosUtil::calculatePercentage).orElse(null))
                .qosTestResultCounters(Optional.ofNullable(basicQosTest)
                        .map(BasicQosTest::getQosTestResultCounters).orElse(Collections.emptyList()))
                .measurementDate(basicTest.getMeasurementDate())
                .device(basicTest.getDevice())
                .loopModeUuid(basicTest.getLoopModeUuid())
                .userExperienceMetrics(userExperienceService.getBasicTestUserExperience(basicTest))
                .radioSignals(Optional.ofNullable(basicTest.getRadioSignals())
                        .orElse(Collections.emptyList()))
                .networkName(BasicTestMobileMapper.getNetworkName(basicTest))
                .measurementServerName(basicTest.getMeasurementServerName())
                .location(BasicTestMobileMapper.getLocation(basicTest))
                .operator(basicTest.getOperator())
                .measurementServerCity(basicTest.getMeasurementServerCity())
                .appVersion(basicTest.getAppVersion())
                .platform(basicTest.getPlatform())
                .build();
    }
}
