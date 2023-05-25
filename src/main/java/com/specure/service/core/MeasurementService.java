package com.specure.service.core;

import com.specure.common.model.jpa.Measurement;
import com.maxmind.geoip2.model.CityResponse;
import com.specure.model.dto.TimeSlot;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.MeasurementRegistrationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface MeasurementService {

    Measurement save(Measurement measurement);

    Measurement partialUpdateMeasurementFromProbeResult(MeasurementRequest measurementRequest, Map<String, String> headers);

    MeasurementRegistrationResponse registerMeasurement(DataForMeasurementRegistration dataForMeasurementRegistration, Map<String, String> headers);

    TimeSlot getTimeSlot(long now);

    MeasurementHistoryResponse getMeasurementDetailByUuid(String uuid);

    Measurement findByOpenTestUuid(String uuid);

    Optional<Measurement> getMeasurementByToken(String token);

    Page<Measurement> findAll(Pageable pageable);

    void setCityDetailsFromIpAddress(Measurement measurement, CityResponse cityResponse);
}
