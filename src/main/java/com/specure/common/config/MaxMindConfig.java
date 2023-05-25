package com.specure.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("maxmind.geoip")
public class MaxMindConfig {
    private int accountId;
    private String licenseKey;
}


