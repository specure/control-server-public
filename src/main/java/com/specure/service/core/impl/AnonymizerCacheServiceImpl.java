package com.specure.service.core.impl;


import com.specure.constant.Config;
import com.specure.service.core.AnonymizerCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnonymizerCacheServiceImpl implements AnonymizerCacheService {
    public static final String ANONYMIZED = "Anonymized";
    private final CacheManager cacheManager;

    @Override
    public String anonymize(String value, String key) {
        if (Objects.isNull(value)) {
            return null;
        }
        Cache anonymizeCache = cacheManager.getCache(Config.ANONYMIZE_CACHE_NAME);
        anonymizeCache.put(key, value);
        return ANONYMIZED;
    }

    @Override
    public String deanonymize(String key) {
        Cache anonymizeCache = cacheManager.getCache(Config.ANONYMIZE_CACHE_NAME);
        return Optional.ofNullable(anonymizeCache.get(key))
                .map(Cache.ValueWrapper::get)
                .map(String.class::cast)
                .orElse(ANONYMIZED);
    }
}
