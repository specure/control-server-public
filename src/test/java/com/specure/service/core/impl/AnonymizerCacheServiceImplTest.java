package com.specure.service.core.impl;

import com.specure.constant.Config;
import com.specure.sah.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AnonymizerCacheServiceImplTest {

    @Mock
    private CacheManager cacheManager;
    @InjectMocks
    private AnonymizerCacheServiceImpl anonymizerCacheService;

    @Mock
    private Cache cache;
    @Mock
    private Cache.ValueWrapper valueWrapper;

    @Test
    void anonymize_whenValueIsNull_expectedNull() {
        var result = anonymizerCacheService.anonymize(null, TestConstants.DEFAULT_KEY);

        assertNull(result);
    }

    @Test
    void anonymize_whenValueIsNotNull_expectedNull() {
        when(cacheManager.getCache(Config.ANONYMIZE_CACHE_NAME)).thenReturn(cache);
        var result = anonymizerCacheService.anonymize(TestConstants.DEFAULT_TEXT, TestConstants.DEFAULT_KEY);

        assertEquals(AnonymizerCacheServiceImpl.ANONYMIZED, result);
        verify(cache).put(TestConstants.DEFAULT_KEY, TestConstants.DEFAULT_TEXT);
    }

    @Test
    void deanonymize_whenValueIsPresent_expectedValue() {
        when(cacheManager.getCache(Config.ANONYMIZE_CACHE_NAME)).thenReturn(cache);
        when(cache.get(TestConstants.DEFAULT_KEY)).thenReturn(valueWrapper);
        when(valueWrapper.get()).thenReturn(TestConstants.DEFAULT_TEXT);

        var result = anonymizerCacheService.deanonymize(TestConstants.DEFAULT_KEY);

        assertEquals(TestConstants.DEFAULT_TEXT, result);
    }

    @Test
    void deanonymize_whenValueIsNotPresent_expectedValue() {
        when(cacheManager.getCache(Config.ANONYMIZE_CACHE_NAME)).thenReturn(cache);
        var result = anonymizerCacheService.deanonymize(TestConstants.DEFAULT_KEY);

        assertEquals(AnonymizerCacheServiceImpl.ANONYMIZED, result);
    }
}
