package com.specure.mapper.mobile;


import com.specure.dto.sah.qos.QosTestResult;
import com.specure.common.model.jpa.qos.QosResult;
import com.specure.response.mobile.QosMeasurementsResponse;

public interface MobileQosTestResultMapper {
    QosMeasurementsResponse.QosTestResultItem toQosTestResultItem(QosTestResult qosTestResult, boolean isOpenTestUuid);

    QosTestResult testResultToQosTestResult(QosResult qosResult);
}
