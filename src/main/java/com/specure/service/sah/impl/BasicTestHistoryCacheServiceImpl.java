package com.specure.service.sah.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.model.jpa.BasicTestHistoryCache;
import com.specure.repository.sah.BasicTestHistoryCacheRepository;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.service.sah.BasicTestHistoryCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicTestHistoryCacheServiceImpl implements BasicTestHistoryCacheService {

    private final BasicTestHistoryCacheRepository basicTestHistoryCacheRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<BasicTestHistoryMobileResponse> getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid(String openTestUuid) {
        return basicTestHistoryCacheRepository.findById(openTestUuid)
                .map(basicTestHistoryCache -> {
                    try {
                        return objectMapper.readValue(basicTestHistoryCache.getBasicTestHistory(), BasicTestHistoryMobileResponse.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    @Override
    public void save(BasicTestHistoryMobileResponse basicTestHistoryMobileResponse) {
        try {
            String cache = objectMapper.writeValueAsString(basicTestHistoryMobileResponse);
            BasicTestHistoryCache basicTestHistoryCache = BasicTestHistoryCache.builder()
                    .openTestUuid(basicTestHistoryMobileResponse.getOpenTestUuid())
                    .basicTestHistory(cache)
                    .build();
            log.info("BasicTestHistoryCacheServiceImpl: save cache {}", cache);
            basicTestHistoryCacheRepository.save(basicTestHistoryCache);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCachedBasicTestHistoryResponse() {
        basicTestHistoryCacheRepository.deleteAll();
    }
}
