package com.specure.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("measurement-server")
public class MeasurementServerConfig {
    private String host;
    private Long slotWindow;
    private Long defaultProviderId;
}
