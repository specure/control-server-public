package com.specure.service.core;

import com.specure.common.model.jpa.MeasurementServerDescription;

public interface MeasurementServerDescriptionService {
    MeasurementServerDescription save(MeasurementServerDescription measurementServerDescription);
    void deleteById(Long id);
    boolean existById(Long id);
}
