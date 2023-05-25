package com.specure.service.core.impl;

import com.specure.multitenant.MultiTenantManager;
import com.specure.service.core.AnonymizerCacheService;
import com.specure.service.core.FieldAnonymizerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FieldAnonymizerFilterImpl implements FieldAnonymizerFilter {
    public static final String WIFI_SSID_SUFFIX = "WIFI_SSID";
    public static final String IP_ADDRESS_SUFFIX = "IP_ADDRESS";
    private final MultiTenantManager multiTenantManager;
    private final AnonymizerCacheService anonymizerCacheService;

    @Override
    public String getIpAddressFilter(String ipAddress, String openTestUuid) {
        if ("no".equals(multiTenantManager.getCurrentTenant())
                || "ratel_rs".equals(multiTenantManager.getCurrentTenant())) {
            return anonymizerCacheService.deanonymize(openTestUuid + IP_ADDRESS_SUFFIX);
        }
        return ipAddress;
    }

    @Override
    public String saveIpAddressFilter(String ipAddress, String openTestUuid) {
        if ("no".equals(multiTenantManager.getCurrentTenant())
                || "ratel_rs".equals(multiTenantManager.getCurrentTenant())) {
            return anonymizerCacheService.anonymize(ipAddress, openTestUuid + IP_ADDRESS_SUFFIX);
        }
        return ipAddress;
    }

    @Override
    public void refreshAnonymizedIpAddress(String newIpAddress, String openTestUuid) {
        if ("no".equals(multiTenantManager.getCurrentTenant())
                || "ratel_rs".equals(multiTenantManager.getCurrentTenant())) {
            String key = openTestUuid + IP_ADDRESS_SUFFIX;
            String oldIpAddress = anonymizerCacheService.deanonymize(key);
            if (Objects.equals(oldIpAddress, AnonymizerCacheServiceImpl.ANONYMIZED)) {
                anonymizerCacheService.anonymize(newIpAddress, key);
            }
        }
    }

    @Override
    public String getWifiSsidFilter(String openTestUuid) {
        if ("ratel_rs".equals(multiTenantManager.getCurrentTenant())) {
            return anonymizerCacheService.deanonymize(openTestUuid + WIFI_SSID_SUFFIX);
        }
        return openTestUuid;
    }

    @Override
    public String saveWifiSsidFilter(String value, String openTestUuid) {
        if ("ratel_rs".equals(multiTenantManager.getCurrentTenant())) {
            return anonymizerCacheService.anonymize(value, openTestUuid + WIFI_SSID_SUFFIX);
        }
        return value;
    }
}
