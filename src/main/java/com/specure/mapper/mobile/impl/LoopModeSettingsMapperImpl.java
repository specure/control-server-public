package com.specure.mapper.mobile.impl;

import com.specure.common.service.UUIDGenerator;
import com.specure.mapper.mobile.LoopModeSettingsMapper;
import com.specure.common.model.jpa.LoopModeSettings;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoopModeSettingsMapperImpl implements LoopModeSettingsMapper {

    private final UUIDGenerator uuidGenerator;

    @Override
    public LoopModeSettings loopModeInfoToLoopModeSettings(MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo) {
        var loopModeSettings = new LoopModeSettings();
        loopModeSettings.setMaxDelay(loopModeInfo.getMaxDelay());
        loopModeSettings.setMaxMovement(loopModeInfo.getMaxMovement());
        loopModeSettings.setMaxTests(loopModeInfo.getMaxTests());
        loopModeSettings.setTestCounter(loopModeInfo.getTestCounter());
        //if no loop mode uuid is set - generate one
        if (loopModeInfo.getLoopUuid() == null)
            loopModeSettings.setLoopUuid(uuidGenerator.generateUUID().toString());
        else
            loopModeSettings.setLoopUuid(loopModeInfo.getLoopUuid());
        //old clients expect a "text_counter"
        if (loopModeInfo.getTestCounter() == null)
            loopModeSettings.setTestCounter(loopModeInfo.getTextCounter());
        return loopModeSettings;
    }
}
