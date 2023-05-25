package com.specure.service.sah;

import com.specure.response.mobile.BasicTestHistoryMobileResponse;

import java.util.Optional;

public interface BasicTestHistoryCacheService {

    Optional<BasicTestHistoryMobileResponse> getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid(String openTestUuid);

    void save(BasicTestHistoryMobileResponse basicTestHistoryMobileResponse);

    void clearCachedBasicTestHistoryResponse();
}
