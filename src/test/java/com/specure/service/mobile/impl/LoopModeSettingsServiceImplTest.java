package com.specure.service.mobile.impl;


import com.specure.mapper.mobile.LoopModeSettingsMapper;
import com.specure.common.model.jpa.LoopModeSettings;
import com.specure.repository.mobile.LoopModeSettingsRepository;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.sah.TestConstants;
import com.specure.service.mobile.LoopModeSettingsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class LoopModeSettingsServiceImplTest {

    @MockBean
    private LoopModeSettingsMapper loopModeSettingsMapper;

    @MockBean
    private LoopModeSettingsRepository loopModeSettingsRepository;

    private LoopModeSettingsService loopModeSettingsService;

    @Mock
    private LoopModeSettings loopModeSettings;
    @Mock
    private MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo;

    @Before
    public void setUp() {
        loopModeSettingsService = new LoopModeSettingsServiceImpl(loopModeSettingsMapper, loopModeSettingsRepository);
    }

    @Test
    public void processLoopModeSettingsInfo_whenCorrectInvoke_expectLoopUuid() {
        when(loopModeSettingsMapper.loopModeInfoToLoopModeSettings(loopModeInfo)).thenReturn(loopModeSettings);
        when(loopModeSettings.getLoopUuid()).thenReturn(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING);

        var response = loopModeSettingsService.processLoopModeSettingsInfo(TestConstants.DEFAULT_CLIENT_UUID_STRING, loopModeInfo, TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);

        verify(loopModeSettings).setClientUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING);
        verify(loopModeSettings).setTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);
        verify(loopModeSettingsRepository).save(loopModeSettings);
        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response);
    }

    @Test
    public void getLoopModeSettingsUuidByOpenTestUuid_whenCorrectInvoke_expectLoopUuid() {
        when(loopModeSettingsRepository.findByTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(Optional.of(loopModeSettings));
        when(loopModeSettings.getLoopUuid()).thenReturn(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING);

        var response = loopModeSettingsService.getLoopModeSettingsUuidByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);

        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response);
    }
}
