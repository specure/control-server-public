package com.specure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("elastic-index")
public class ElasticIndexTenantConfig {
    private Map<String, String> basicTenantIndexes;
    private Map<String, String> basicQosTenantIndexes;
    private Map<String, String> netNeutralityIndexes;
    private Map<String, ElasticsearchCredential> elasticCredential;

    @Getter
    @Setter
    public static class ElasticsearchCredential {
        private String host;
        private String username;
        private String password;
        private Boolean isSSL;
        private Boolean isTLS;
    }
}


