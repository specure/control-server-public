package com.specure.service.core;

public interface AnonymizerCacheService {

    String anonymize(String value, String openTestUuid);

    String deanonymize(String key);
}
