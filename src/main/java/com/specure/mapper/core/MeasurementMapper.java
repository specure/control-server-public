package com.specure.mapper.core;

import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.RawProvider;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import org.mapstruct.MappingTarget;

public interface MeasurementMapper {
    Measurement measurementRequestToMeasurement(MeasurementRequest measurementResultRequest);

    MeasurementHistoryResponse measurementToMeasurementHistoryResponse(Measurement measurement);

    Measurement updateMeasurementProviderInfo(@MappingTarget Measurement measurement, RawProvider provider);
}
