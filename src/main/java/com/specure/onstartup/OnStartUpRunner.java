package com.specure.onstartup;

import com.specure.config.ClientTenantConfig;
import com.specure.constant.Constants;
import com.specure.multitenant.MultiTenantManager;
import com.specure.service.sah.BasicTestHistoryCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnStartUpRunner implements ApplicationRunner {

    private final ClientTenantConfig dataSourceConfig;
    private final MultiTenantManager multiTenantManager;
    private final BasicTestHistoryCacheService basicTestHistoryCacheService;

    @Scheduled(cron = "0 0 * ? * *")
    public void clearBasicTestHistoryMobileCache() {
        log.info("Clear basic test history mobile cache job started");
        multiTenantManager.getTenantDataSources()
                .keySet().stream()
                .map(Object::toString)
                .filter(Predicate.not(x -> x.equalsIgnoreCase(Constants.ADMIN_TENANT_NAME)))
                .forEach(tenant -> {
                    multiTenantManager.setCurrentTenant(tenant);
                    basicTestHistoryCacheService.clearCachedBasicTestHistoryResponse();
                });
    }

    @Override
    public void run(ApplicationArguments args) {

        var clientTenants = new HashSet<>(dataSourceConfig.getClientTenantMapping().keySet());
        clientTenants.remove(Constants.ADMIN_TENANT_NAME);
        multiTenantManager.addTenant(Constants.ADMIN_TENANT_NAME);
        clientTenants.forEach(key -> {
            multiTenantManager.addTenant(key);
            multiTenantManager.addTenantElasticsearch(key);
        });
    }
}
