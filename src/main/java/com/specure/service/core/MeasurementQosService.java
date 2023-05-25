package com.specure.service.core;

import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.response.core.measurement.qos.response.MeasurementQosParametersResponse;

public interface MeasurementQosService {
    void saveMeasurementQos(MeasurementQosRequest measurementQosRequest);
    MeasurementQosParametersResponse getQosParameters(MeasurementQosParametersRequest measurementQosParametersRequest);
    public void deleteByOpenUUID(String uuid);
}
