package com.specure.multitenant;

import com.specure.config.ClientTenantConfig;
import com.specure.constant.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultiTenantFlywayMigrationStrategy implements FlywayMigrationStrategy {

    private final ClientTenantConfig clientTenantConfig;

    @Override
    public void migrate(Flyway flyway) {
        migrateAdminDatabase();
        migrateClientDatabases();
    }

    private void migrateClientDatabases() {
        clientTenantConfig.getClientTenantMapping().entrySet()
                .stream()
                .filter(Predicate.not(x -> x.getKey().equals(Constants.ADMIN_TENANT_NAME)))
                .map(Map.Entry::getValue)
                .forEach(tenantConfig -> {
                    try {
                        configureAndMigrate(tenantConfig, "classpath:db/migration/client");
                    } catch (Exception e) {
                        log.error("Unable to migrate client with URL: {}", tenantConfig.getUrl());
                    }
                });
    }

    private void migrateAdminDatabase() {
        clientTenantConfig.getClientTenantMapping().entrySet()
                .stream()
                .filter(x -> x.getKey().equals(Constants.ADMIN_TENANT_NAME))
                .map(Map.Entry::getValue)
                .forEach(adminTenantConfig -> {
                    try {
                        configureAndMigrate(adminTenantConfig, "classpath:db/migration/admin");
                    } catch (Exception e) {
                        log.error("Unable to migrate admin database", e);
                    }
                });

    }

    private void configureAndMigrate(ClientTenantConfig.DatasourceClientCredential tenantCredential, String scriptsLocation) {
        Flyway.configure()
                .locations(scriptsLocation)
                .validateOnMigrate(false)
                .table("_SCHEMA_VERSION")
                .baselineOnMigrate(true)
                .outOfOrder(false)
                .dataSource(tenantCredential.getUrl(),
                        tenantCredential.getUsername(),
                        tenantCredential.getPassword())
                .load()
                .migrate();
    }
}
