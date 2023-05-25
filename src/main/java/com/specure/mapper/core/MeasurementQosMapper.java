package com.specure.mapper.core;

import com.specure.common.model.jpa.MeasurementQos;
import com.specure.request.core.MeasurementQosRequest;


public interface MeasurementQosMapper {
    MeasurementQos measurementQosRequestToMeasurementQos(MeasurementQosRequest measurementQosRequest);

    MeasurementQos measurementQosRequestToMeasurementQosMobile(MeasurementQosRequest measurementQosRequest);
}
