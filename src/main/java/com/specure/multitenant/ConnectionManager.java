package com.specure.multitenant;

import com.specure.config.ClientTenantConfig;
import com.specure.config.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
@AllArgsConstructor
public class ConnectionManager {

    private DataSourceConfig dataSourceProperties;
    private ClientTenantConfig clientTenantConfig;

    public Connection checkConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource buildDataSource(String tenantId) {
        HikariConfig config = new HikariConfig();
        config.setMinimumIdle(dataSourceProperties.getMinIdle());
        config.setMaximumPoolSize(dataSourceProperties.getPoolSize());
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        config.setJdbcUrl(clientTenantConfig.getClientTenantMapping().get(tenantId).getUrl());
        config.setUsername(clientTenantConfig.getClientTenantMapping().get(tenantId).getUsername());
        config.setPassword(clientTenantConfig.getClientTenantMapping().get(tenantId).getPassword());
        return new HikariDataSource(config);
    }
}
