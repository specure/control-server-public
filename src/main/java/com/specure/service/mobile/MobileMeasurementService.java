package com.specure.service.mobile;

import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementResultDetailRequest;
import com.specure.request.mobile.MobileMeasurementResultRequest;
import com.specure.response.mobile.MeasurementResultMobileResponse;
import com.specure.response.mobile.MobileMeasurementResultContainerResponse;
import com.specure.response.mobile.MobileMeasurementResultDetailResponse;

import java.util.Map;

public interface MobileMeasurementService {
    MeasurementResultMobileResponse processMeasurementResult(MeasurementResultMobileRequest measurementResultRequest, Map<String, String> headers);

    MobileMeasurementResultContainerResponse getTestResult(MobileMeasurementResultRequest mobileMeasurementResultRequest);

    MobileMeasurementResultDetailResponse getTestResultDetailByTestUUID(MobileMeasurementResultDetailRequest mobileMeasurementResultDetailRequest);
}
