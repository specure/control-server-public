package com.specure.service.mobile;

import com.specure.request.mobile.MobileMeasurementSettingRequest;

public interface LoopModeSettingsService {

    String processLoopModeSettingsInfo(String clientUuid, MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo, String testOpenUuid);

    String getLoopModeSettingsUuidByOpenTestUuid(String testUuid);
}
