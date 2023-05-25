package com.specure.service.mobile;


import com.specure.request.core.CapabilitiesRequest;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.response.mobile.MobileMeasurementQosResponse;
import com.specure.response.mobile.OverallQosMeasurementResponse;
import com.specure.response.mobile.QosMeasurementsResponse;

import java.util.Map;
import java.util.UUID;

public interface MobileQosMeasurementService {

    OverallQosMeasurementResponse saveQosMeasurementResult(MeasurementQosRequest mobileQosResultRequest);

    QosMeasurementsResponse getQosResult(UUID qosTestUuid, String language, CapabilitiesRequest capabilitiesRequest);

    QosMeasurementsResponse evaluateQosByOpenTestUUID(UUID openTestUUID, String lang);

    MobileMeasurementQosResponse getQosParameters(MeasurementQosParametersRequest measurementQosParametersRequest, Map<String, String> headers);
}
