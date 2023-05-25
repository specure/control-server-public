package com.specure.mapper.mobile.impl;

import com.specure.common.service.UUIDGenerator;
import com.specure.mapper.mobile.LoopModeSettingsMapper;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.sah.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LoopModeSettingsMapperImplTest {

    @MockBean
    private UUIDGenerator uuidGenerator;

    private LoopModeSettingsMapper loopModeSettingsMapper;

    @Mock
    private MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo;

    @BeforeEach
    void setUp() {
        loopModeSettingsMapper = new LoopModeSettingsMapperImpl(uuidGenerator);
    }

    @Test
    void loopModeInfoToLoopModeSettings_whenCorrectInvoke_expectLoopModeSettings() {
        when(uuidGenerator.generateUUID()).thenReturn(TestConstants.DEFAULT_LOOP_MODE_UUID);
        when(loopModeInfo.getMaxDelay()).thenReturn(TestConstants.DEFAULT_MAX_DELAY);
        when(loopModeInfo.getMaxMovement()).thenReturn(TestConstants.DEFAULT_MAX_MOVEMENTS);
        when(loopModeInfo.getMaxTests()).thenReturn(TestConstants.DEFAULT_MAX_TESTS);
        when(loopModeInfo.getTestCounter()).thenReturn(TestConstants.DEFAULT_TEST_COUNTER);

        var response = loopModeSettingsMapper.loopModeInfoToLoopModeSettings(loopModeInfo);

        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response.getLoopUuid());
        assertEquals(TestConstants.DEFAULT_MAX_DELAY, response.getMaxDelay());
        assertEquals(TestConstants.DEFAULT_MAX_MOVEMENTS, response.getMaxMovement());
        assertEquals(TestConstants.DEFAULT_MAX_TESTS, response.getMaxTests());
        assertEquals(TestConstants.DEFAULT_TEST_COUNTER, response.getTestCounter());
    }

    @Test
    void loopModeInfoToLoopModeSettings_whenLoopUuidIsNotNull_expectLoopModeSettings() {
        when(loopModeInfo.getLoopUuid()).thenReturn(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING);
        when(loopModeInfo.getMaxDelay()).thenReturn(TestConstants.DEFAULT_MAX_DELAY);
        when(loopModeInfo.getMaxMovement()).thenReturn(TestConstants.DEFAULT_MAX_MOVEMENTS);
        when(loopModeInfo.getMaxTests()).thenReturn(TestConstants.DEFAULT_MAX_TESTS);
        when(loopModeInfo.getTestCounter()).thenReturn(TestConstants.DEFAULT_TEST_COUNTER);

        var response = loopModeSettingsMapper.loopModeInfoToLoopModeSettings(loopModeInfo);

        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response.getLoopUuid());
        assertEquals(TestConstants.DEFAULT_MAX_DELAY, response.getMaxDelay());
        assertEquals(TestConstants.DEFAULT_MAX_MOVEMENTS, response.getMaxMovement());
        assertEquals(TestConstants.DEFAULT_MAX_TESTS, response.getMaxTests());
        assertEquals(TestConstants.DEFAULT_TEST_COUNTER, response.getTestCounter());
    }

    @Test
    void loopModeInfoToLoopModeSettings_whenTestCounterIsNull_expectLoopModeSettings() {
        when(loopModeInfo.getLoopUuid()).thenReturn(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING);
        when(loopModeInfo.getMaxDelay()).thenReturn(TestConstants.DEFAULT_MAX_DELAY);
        when(loopModeInfo.getMaxMovement()).thenReturn(TestConstants.DEFAULT_MAX_MOVEMENTS);
        when(loopModeInfo.getMaxTests()).thenReturn(TestConstants.DEFAULT_MAX_TESTS);
        when(loopModeInfo.getTestCounter()).thenReturn(null);
        when(loopModeInfo.getTextCounter()).thenReturn(TestConstants.DEFAULT_TEST_COUNTER);

        var response = loopModeSettingsMapper.loopModeInfoToLoopModeSettings(loopModeInfo);

        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response.getLoopUuid());
        assertEquals(TestConstants.DEFAULT_MAX_DELAY, response.getMaxDelay());
        assertEquals(TestConstants.DEFAULT_MAX_MOVEMENTS, response.getMaxMovement());
        assertEquals(TestConstants.DEFAULT_MAX_TESTS, response.getMaxTests());
        assertEquals(TestConstants.DEFAULT_TEST_COUNTER, response.getTestCounter());
    }
}
