package com.specure.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("app.rmbt")
public class ApplicationProperties {
    private LanguageProperties language;
    private Set<String> clientNames;
    private String version;
    private Integer threads;
    private Integer duration;
    private Integer pings;
    private Integer accuracyDetailLimit;
    private Integer accuracyButtonLimit;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LanguageProperties {
        private Set<String> supportedLanguages;
        private String defaultLanguage;
    }
}
