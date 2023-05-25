package com.specure.service.sah.impl;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.common.model.jpa.SpeedDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.constant.Config;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.repository.sah.SpeedDetailRepository;
import com.specure.service.sah.SpeedDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeedDetailServiceImpl implements SpeedDetailService {
    private final SpeedDetailRepository speedDetailRepository;
    private final SpeedDetailMapper speedDetailMapper;
    private final RedisCacheManager cacheManager;
    private final ObjectMapper objectMapper;


    @Override
    public List<SimpleSpeedDetail> getSpeedDetailsBy(String openTestUuid) {
        return Optional.ofNullable(fromRedisCache(openTestUuid))
                .orElseGet(() -> fromMySql(openTestUuid));
    }

    @Override
    public void saveSpeedDetailsToCache(String openTestUuid, List<SpeedDetail> speedDetails) {
        List<SimpleSpeedDetail> simpleSpeedDetails = speedDetails.stream()
                .map(speedDetailMapper::speedDetailToSimpleSpeedDetail)
                .collect(Collectors.toList());
        String serializedList = serializeSimpleSpeedDetailList(simpleSpeedDetails);
        cacheManager.getCache(Config.SPEED_DETAIL_CACHE_NAME).put(openTestUuid, serializedList);
    }


    private List<SimpleSpeedDetail> fromMySql(String openTestUuid) {
        return speedDetailRepository.findAllByMeasurement_OpenTestUuid(openTestUuid).stream()
                .map(speedDetailMapper::speedDetailToSimpleSpeedDetail)
                .collect(Collectors.toList());
    }

    private List<SimpleSpeedDetail> fromRedisCache(String openTestUuid) {
        Cache cache = cacheManager.getCache(Config.SPEED_DETAIL_CACHE_NAME);
        return Optional.ofNullable(cache.get(openTestUuid))
                .map(Cache.ValueWrapper::get)
                .map(String.class::cast)
                .map(this::deserializeSimpleSpeedDetailList)
                .orElse(null);
    }

    private String serializeSimpleSpeedDetailList(List<SimpleSpeedDetail> simpleSpeedDetails) {
        try {
            return objectMapper.writeValueAsString(simpleSpeedDetails);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SimpleSpeedDetail> deserializeSimpleSpeedDetailList(String x) {
        try {
            return objectMapper.readValue(x, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
