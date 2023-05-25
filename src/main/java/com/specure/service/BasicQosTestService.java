package com.specure.service;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.jpa.MeasurementQos;

import java.util.Collection;
import java.util.List;

public interface BasicQosTestService {
    List<BasicQosTest> getBasicQosTestsByBasicTestUuid(Collection<String> uuid);

    BasicQosTest getBasicQosTestByOpenTestUuid(String uuid);

    void saveMeasurementQosToElastic(MeasurementQos measurementQos);

    BasicQosTest saveMeasurementQosMobileToElastic(MeasurementQos measurementQos);
}
