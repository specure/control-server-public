package com.specure.mapper.mobile.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.dto.sah.qos.QosTestResult;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.common.model.jpa.qos.QosResult;
import com.specure.response.mobile.QosMeasurementsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MobileQosTestResultMapperImpl implements MobileQosTestResultMapper {

    private final ObjectMapper objectMapper;

    @Override
    public QosMeasurementsResponse.QosTestResultItem toQosTestResultItem(QosTestResult qosTestResult, boolean isOpenTestUuid) {
        QosMeasurementsResponse.QosTestResultItem.QosTestResultItemBuilder builder = QosMeasurementsResponse.QosTestResultItem.builder()
                .uid(qosTestResult.getId())
                .testType(qosTestResult.getQosTestObjective().getTestType())
                .result(getResult(qosTestResult))
                .testDesc(qosTestResult.getTestDescription())
                .successCount(qosTestResult.getSuccessCount())
                .failureCount(qosTestResult.getFailureCount())
                .testSummary(qosTestResult.getTestSummary())
                .testResultKeys(qosTestResult.getResultKeyMap().keySet())
                .testResultKeyMap(qosTestResult.getResultKeyMap());

//        if (!isOpenTestUuid) {
//            builder.nnTestUid(qosTestResult.getQosTestObjective().getId().longValue())
//                    .qosTestUid(qosTestResult.getQosTestObjective().getId().longValue())
//                    .testUid(qosTestResult.getTestUid());
//        }

        return builder.build();
    }

    @Override
    public QosTestResult testResultToQosTestResult(QosResult qosResult) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(qosResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return QosTestResult.builder()
                .id(qosResult.getId())
                .successCount(qosResult.getSuccessCount())
                .failureCount(qosResult.getFailureCount())
                .qosTestObjectiveId(qosResult.getQosTestUid())
                .result(result)
                .build();
    }

    private Map<String, Object> getResult(QosTestResult qosTestResult) {
        try {
            return objectMapper.readValue(qosTestResult.getResult(), new TypeReference<>() {
            });
        } catch (Exception e) {
            return null;
        }
    }
}
