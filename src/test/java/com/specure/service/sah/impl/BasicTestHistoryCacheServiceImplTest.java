package com.specure.service.sah.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.model.jpa.BasicTestHistoryCache;
import com.specure.repository.sah.BasicTestHistoryCacheRepository;
import com.specure.sah.TestConstants;
import com.specure.service.sah.BasicTestHistoryCacheService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasicTestHistoryCacheServiceImplTest {

    private BasicTestHistoryCacheService basicTestHistoryCacheService;

    @MockBean
    private BasicTestHistoryCacheRepository basicTestHistoryCacheRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private BasicTestHistoryCache basicTestHistoryCache;
    @Captor
    private ArgumentCaptor<BasicTestHistoryCache> basicTestHistoryCacheArgumentCaptor;

    @Before
    public void setUp() {
        basicTestHistoryCacheService = new BasicTestHistoryCacheServiceImpl(basicTestHistoryCacheRepository, objectMapper);
    }

    @Test
    public void getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid_whenCommonData_expectBasicTestHistoryMobileResponse() {
        when(basicTestHistoryCacheRepository.findById(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(Optional.of(basicTestHistoryCache));
        when(basicTestHistoryCache.getBasicTestHistory()).thenReturn(TestConstants.DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE_JSON);

        var response = basicTestHistoryCacheService.getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);

        assertTrue(response.isPresent());
        assertEquals(TestConstants.DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE, response.get());
    }

    @Test
    public void save_whenCommonData_expectSaved() {
        var basicTestHistoryMobileResponse = TestConstants.DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE;

        basicTestHistoryCacheService.save(basicTestHistoryMobileResponse);

        verify(basicTestHistoryCacheRepository).save(basicTestHistoryCacheArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_BASIC_TEST_HISTORY_MOBILE_RESPONSE_JSON, basicTestHistoryCacheArgumentCaptor.getValue().getBasicTestHistory());
        assertEquals(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING, basicTestHistoryCacheArgumentCaptor.getValue().getOpenTestUuid());
    }
}
