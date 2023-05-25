package com.specure.service.mobile;

import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MobileMeasurementRegistrationService {
    @Transactional
    MobileMeasurementRegistrationResponse registerMobileMeasurement(MobileMeasurementSettingRequest testSettingsRequest, Map<String, String> headers, HttpServletRequest request);
}
