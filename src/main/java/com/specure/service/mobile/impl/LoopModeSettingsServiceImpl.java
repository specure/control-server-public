package com.specure.service.mobile.impl;

import com.specure.mapper.mobile.LoopModeSettingsMapper;
import com.specure.common.model.jpa.LoopModeSettings;
import com.specure.repository.mobile.LoopModeSettingsRepository;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.service.mobile.LoopModeSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoopModeSettingsServiceImpl implements LoopModeSettingsService {

    private final LoopModeSettingsMapper loopModeSettingsMapper;

    private final LoopModeSettingsRepository loopModeSettingsRepository;

    @Override
    public String processLoopModeSettingsInfo(String clientUuid, MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo, String testOpenUuid) {
        LoopModeSettings loopModeSettings = loopModeSettingsMapper.loopModeInfoToLoopModeSettings(loopModeInfo);
        loopModeSettings.setClientUuid(clientUuid);
        loopModeSettings.setTestUuid(testOpenUuid);
        loopModeSettingsRepository.save(loopModeSettings);
        return loopModeSettings.getLoopUuid();
    }

    @Override
    public String getLoopModeSettingsUuidByOpenTestUuid(String testUuid) {
        return loopModeSettingsRepository.findByTestUuid(testUuid)
                .map(LoopModeSettings::getLoopUuid)
                .orElse(null);
    }
}
