package com.specure.service.mobile;

import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.MeasurementHistoryLoopUuidResponse;
import com.specure.response.mobile.MeasurementHistoryMobileResponse;
import org.springframework.data.domain.Pageable;

public interface MeasurementHistoryMobileService {

    MeasurementHistoryMobileResponse getMeasurementHistoryMobileResponse(Pageable pageable, MeasurementHistoryMobileRequest measurementHistoryMobileRequest);

    BasicTestHistoryMobileResponse getMeasurementHistoryMobileResponseByUuid(String uuid);

    MeasurementHistoryLoopUuidResponse getMeasurementHistoryMobileResponseLoopUuidAggregation(Pageable pageable, MeasurementHistoryMobileRequest measurementHistoryMobileRequest);
}
