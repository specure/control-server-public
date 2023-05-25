package com.specure.config;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@Slf4j
public class ParserConfig {

    @Bean
    public UserAgentParser userAgentParser() {
        try {
            return new UserAgentService().loadParser(Collections.singletonList(BrowsCapField.BROWSER));
        } catch (Exception e) {
            log.error("Unable to initialize blueconic parser.");
            return null;
        }
    }

}
