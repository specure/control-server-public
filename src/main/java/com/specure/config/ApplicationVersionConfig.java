package com.specure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application-version")
@Getter
@Setter
public class ApplicationVersionConfig {

    private String bucketPath;

    private String fileName;
}
