package com.specure.mapper.mobile;

import com.specure.common.model.jpa.Measurement;
import com.specure.request.mobile.MeasurementResultMobileRequest;

public interface MobileMeasurementMapper {

    Measurement measurementMobileResultRequestToMeasurement(MeasurementResultMobileRequest measurementResultMobileRequest, Measurement measurement);
}
