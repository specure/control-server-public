package com.specure.mapper.mobile;

import com.specure.common.model.jpa.LoopModeSettings;
import com.specure.request.mobile.MobileMeasurementSettingRequest;

public interface LoopModeSettingsMapper {

    LoopModeSettings loopModeInfoToLoopModeSettings(MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo);
}
