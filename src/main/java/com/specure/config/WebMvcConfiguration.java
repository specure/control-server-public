package com.specure.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.specure.constant.URIConstants.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @KeycloakConfiguration
    public static class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobal(
                AuthenticationManagerBuilder auth) {

            KeycloakAuthenticationProvider keycloakAuthenticationProvider
                    = keycloakAuthenticationProvider();
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                    new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(
                    new SessionRegistryImpl());
        }


        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            super.configure(httpSecurity);
            httpSecurity.csrf().disable().authorizeRequests()
                    .antMatchers(TEST_RESULT, TEST_RESULT_DETAIL, RESULT).permitAll()
                    .antMatchers(REQUEST_DATA_COLLECTOR).permitAll()
                    .antMatchers(HttpMethod.GET, MEASUREMENT_SERVER).permitAll()
                    .antMatchers(TEST_REQUEST, TEST_REQUEST_FOR_WEB_CLIENT, TEST_REQUEST_FOR_ADMIN, MEASUREMENT_RESULT, MEASUREMENT_RESULT_QOS, MEASUREMENT_QOS_REQUEST, MEASUREMENT_RESULT_BY_UUID, MEASUREMENT_RESULT_BY_UUID_FROM_DB).permitAll()
                    .antMatchers(SETTINGS).permitAll()
                    .antMatchers(SETTINGS_MOBILE).permitAll()
                    .antMatchers(SIGNAL_REQUEST).permitAll()
                    .antMatchers(MOBILE + TEST_REQUEST).permitAll()
                    .antMatchers(MOBILE + TEST_RESULT).permitAll()
                    .antMatchers(MOBILE + TEST_RESULT_DETAIL).permitAll()
                    .antMatchers(MOBILE + RESULT).permitAll()
                    .antMatchers(MOBILE + MEASUREMENT_QOS_REQUEST).permitAll()
                    .antMatchers(NET_NEUTRALITY_TEST_REQUEST).permitAll()
                    .antMatchers(NET_NEUTRALITY_RESULT).permitAll()
                    .antMatchers(NET_NEUTRALITY_RESULT_BY_UUID).permitAll()
                    .antMatchers(MOBILE + MEASUREMENT_QOS_RESULT).permitAll()
                    .antMatchers(MOBILE + RESULT_QOS_URL).permitAll()
                    .antMatchers(MOBILE + QOS_BY_OPEN_TEST_UUID_AND_LANGUAGE).permitAll()
                    .antMatchers(MOBILE + QOS_BY_OPEN_TEST_UUID).permitAll()
                    .antMatchers(MOBILE + SETTINGS).permitAll()
                    .antMatchers(MOBILE + HISTORY).permitAll()
                    .antMatchers(V2 + MOBILE + HISTORY).permitAll()
                    .antMatchers(MOBILE + HISTORY_BY_UUID).permitAll()
                    .antMatchers(MOBILE + IP).permitAll()
                    .antMatchers(VERSION).permitAll()
                    .antMatchers(IP).permitAll()
                    .antMatchers(EXPORT_MOBILE_FULL, EXPORT_MOBILE_MONTHLY).permitAll()
                    .antMatchers(EXPORT_FULL, EXPORT_MONTHLY).permitAll()
                    .antMatchers(HttpMethod.GET, HEALTH).permitAll()
                    .antMatchers(HttpMethod.GET, NATIONAL_TABLE).permitAll()
                    .antMatchers(NATIONAL_OPERATOR).permitAll()
                    .antMatchers(NATIONAL_OPERATOR_BY_ID).permitAll()
                    .anyRequest().authenticated();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            // ignoring security for swagger APIs
            web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/health").and()
                    .ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        }
    }
}
