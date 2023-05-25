package com.specure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("rabbit-mq")
public class RabbitMqConfig {
    private String alivePortTopic;
    private String deadPortTopic;
    private String changeAdHocCampaignTopic;
    private String startStopAdHocCampaignTopic;
    private String updateRawProviderMappingTopic;

    @Bean
    public Queue queueKeepAlive() {
        return new Queue(alivePortTopic, true);
    }

    @Bean
    public Queue queueAdHocCampaign() {
        return new Queue(changeAdHocCampaignTopic, true);
    }

    @Bean
    public Queue startStopAdHocCampaignTopic() {
        return new Queue(startStopAdHocCampaignTopic, true);
    }

    @Bean
    public Queue deadPortTopic() {
        return new Queue(deadPortTopic, true);
    }

    @Bean
    public Queue updateRawProviderMappingTopic() {
        return new Queue(updateRawProviderMappingTopic, true, false, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
