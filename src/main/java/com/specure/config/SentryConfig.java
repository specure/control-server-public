package com.specure.config;

import io.sentry.Sentry;
import io.sentry.spring.SentryExceptionResolver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sentry")
@Profile(value = {"prod"})
public class SentryConfig {

    @Value("{sentry.dsn}")
    public String dsn;


    @PostConstruct
    private void initializeSentry() {
        if (dsn != null) {
            Sentry.init(dsn);
        }
    }

    @Bean
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new SentryExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Object handler,
                                                 Exception ex) {
                super.resolveException(request, response, handler, ex);
                return null;
            }
        };
    }

    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new io.sentry.spring.SentryServletContextInitializer();
    }

}
