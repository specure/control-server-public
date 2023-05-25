package com.specure.multitenant;

import com.specure.config.ClientTenantConfig;
import com.specure.config.ElasticIndexTenantConfig;
import com.specure.constant.ErrorMessage;
import com.specure.exception.WrongTenantException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MultiTenantManager {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private final ThreadLocal<List<String>> currentCountries = new ThreadLocal<>();
    private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
    private final Map<String, ElasticsearchOperations> tenantElasticsearchOperations = new ConcurrentHashMap<>();
    private final Map<String, RestHighLevelClient> tenantRestHighLevelClient = new ConcurrentHashMap<>();

    private ConnectionManager connectionManager;
    private AbstractRoutingDataSource multiTenantDataSource;
    private ElasticIndexTenantConfig elasticIndexTenantConfig;
    private ClientTenantConfig clientTenantConfig;

    public MultiTenantManager(ElasticIndexTenantConfig elasticIndexTenantConfig, ConnectionManager connectionManager, ClientTenantConfig clientTenantConfig) {
        this.elasticIndexTenantConfig = elasticIndexTenantConfig;
        this.connectionManager = connectionManager;
        this.clientTenantConfig = clientTenantConfig;
    }

    @Bean
    public DataSource dataSource() {
        multiTenantDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return currentTenant.get();
            }
        };
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }

    @SneakyThrows
    public String addTenant(String tenantId) {
        if (!StringUtils.isBlank(tenantId) && !isTenantExist(tenantId)) {
            DataSource dataSource = null;
            Connection testConnection = null;
            try {
                dataSource = connectionManager.buildDataSource(tenantId);
                testConnection = connectionManager.checkConnection(dataSource);
                tenantDataSources.put(tenantId, dataSource);
                multiTenantDataSource.afterPropertiesSet();
            } catch (Exception message) {
                log.error(String.format("Unable to connect ot db %s: %s", tenantId, message));
            } finally {
                if (testConnection != null) {
                    testConnection.close();
                }
            }
        }
        return tenantId;
    }

    public void addTenantElasticsearch(String tenantId) {
        if (!StringUtils.isBlank(tenantId) && !isTenantElasticExist(tenantId)) {
            try {
                RestHighLevelClient client = getElasticClient(tenantId);
                tenantRestHighLevelClient.put(tenantId, client);
                ElasticsearchOperations elasticsearchOperations = new ElasticsearchRestTemplate(client);
                tenantElasticsearchOperations.put(tenantId, elasticsearchOperations);
            } catch (Exception e) {
                log.error(String.format("Unable to connect elastic ot db %s: %s", tenantId, e));
            }
        }
    }


    public void setCurrentCountry(Enumeration<String> countries) {
        if (Objects.isNull(countries) || !countries.hasMoreElements()) {
            currentCountries.set(Collections.emptyList());
        } else {
            currentCountries.set(getFilteredCountryList(countries));
        }
    }


    public void setCurrentTenant(String client) {
        ClientTenantConfig.DatasourceClientCredential tenant = clientTenantConfig.getClientTenantMapping().get(client);
        currentTenant.set(client);
    }

    public String getCurrentTenant() {
        return currentTenant.get();
    }

    public List<String> getCurrentCountries() {
        return Optional.ofNullable(currentCountries.get())
                .orElse(Collections.emptyList());
    }

    public ElasticsearchOperations getCurrentTenantElastic() {
        return Optional.ofNullable(currentTenant.get())
                .map(tenantElasticsearchOperations::get)
                .orElseThrow(() -> new WrongTenantException(String.format(ErrorMessage.WRONG_TENANT, currentTenant.get())));
    }

    public RestHighLevelClient getCurrentTenantRestClient() {
        return tenantRestHighLevelClient.get(getCurrentTenant());
    }

    public Map<String, RestHighLevelClient> getTenantRestClients() {
        return tenantRestHighLevelClient;
    }

    public String getCurrentTenantBasicIndex() {
        return elasticIndexTenantConfig.getBasicTenantIndexes().get(getCurrentTenant());
    }

    public String getCurrentTenantQosIndex() {
        return elasticIndexTenantConfig.getBasicQosTenantIndexes().get(getCurrentTenant());
    }

    public String getCurrentTenantNetNeutralityIndex() {
        return elasticIndexTenantConfig.getNetNeutralityIndexes().get(getCurrentTenant());
    }

    public boolean isTenantExist(String tenantId) {
        return tenantDataSources.containsKey(tenantId);
    }

    public boolean isTenantElasticExist(String tenantId) {
        return tenantElasticsearchOperations.containsKey(tenantId);
    }

    public Map<Object, Object> getTenantDataSources() {
        return tenantDataSources;
    }

    public Set<String> getElasticsearchTenants() {
        return tenantElasticsearchOperations.keySet();
    }

    private List<String> getFilteredCountryList(Enumeration<String> countries) {
        return Collections.list(countries)
                .stream()
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
    }

    private RestHighLevelClient getElasticClient(String tenantId) {
        ClientConfiguration configuration = getElasticClientConfiguration(elasticIndexTenantConfig
                .getElasticCredential()
                .get(tenantId));
        return RestClients.create(configuration).rest();
    }

    private ClientConfiguration getElasticClientConfiguration(ElasticIndexTenantConfig.ElasticsearchCredential
                                                                      elasticsearchCredential) {
        var clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchCredential.getHost());

        if (Boolean.TRUE.equals(elasticsearchCredential.getIsSSL())) {
            clientConfiguration.usingSsl();
        }
        if (Objects.nonNull(elasticsearchCredential.getUsername()) && Objects.nonNull(elasticsearchCredential.getPassword())) {
            clientConfiguration
                    .withBasicAuth(elasticsearchCredential.getUsername(), elasticsearchCredential.getPassword());
        }
        if (Boolean.TRUE.equals(elasticsearchCredential.getIsTLS())) {
            SSLContext sc = null;
            try {
                sc = SSLContext.getInstance("TLS");
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                sc.init(null, trustAllCerts, new SecureRandom());
            } catch (Exception e) {
                e.printStackTrace();
            }
            clientConfiguration.usingSsl(sc, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }

        clientConfiguration.withSocketTimeout(60000);
        clientConfiguration.withConnectTimeout(50000);
        return clientConfiguration.build();
    }
}
