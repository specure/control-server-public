package com.specure.service.sah.impl;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.common.model.jpa.SpeedDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.constant.Config;
import com.specure.core.TestConstants;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.repository.sah.SpeedDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SpeedDetailServiceImplTest {

    @Mock
    private SpeedDetailRepository speedDetailRepository;
    @Mock
    private SpeedDetailMapper speedDetailMapper;
    @Mock
    private RedisCacheManager cacheManager;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private SpeedDetailServiceImpl speedDetailService;

    @Mock
    private Cache speedDetailCache;
    @Mock
    private Cache.ValueWrapper cacheValueWrapper;
    @Mock
    private SpeedDetail speedDetail;
    @Mock
    private SimpleSpeedDetail simpleSpeedDetail;


    @Test
    void getSpeedDetailsBy_fromRedis_expectedSimpleSpeedDetails() throws JsonProcessingException {
        when(cacheManager.getCache(Config.SPEED_DETAIL_CACHE_NAME)).thenReturn(speedDetailCache);
        when(speedDetailCache.get(TestConstants.DEFAULT_OPEN_TEST_UUID)).thenReturn(cacheValueWrapper);
        when(cacheValueWrapper.get()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(objectMapper.readValue(eq(TestConstants.DEFAULT_TEXT), any(TypeReference.class))).thenReturn(List.of(simpleSpeedDetail));
        when(speedDetailMapper.speedDetailToSimpleSpeedDetail(speedDetail)).thenReturn(simpleSpeedDetail);


        var result = speedDetailService.getSpeedDetailsBy(TestConstants.DEFAULT_OPEN_TEST_UUID);

        assertEquals(List.of(simpleSpeedDetail), result);
    }

    @Test
    void getSpeedDetailsBy_fromMySql_expectedSimpleSpeedDetails() {
        when(cacheManager.getCache(Config.SPEED_DETAIL_CACHE_NAME)).thenReturn(speedDetailCache);
        when(speedDetailCache.get(TestConstants.DEFAULT_OPEN_TEST_UUID)).thenReturn(null);
        when(speedDetailRepository.findAllByMeasurement_OpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID)).thenReturn(List.of(speedDetail));
        when(speedDetailMapper.speedDetailToSimpleSpeedDetail(speedDetail)).thenReturn(simpleSpeedDetail);

        var result = speedDetailService.getSpeedDetailsBy(TestConstants.DEFAULT_OPEN_TEST_UUID);

        assertEquals(List.of(simpleSpeedDetail), result);
    }
}
