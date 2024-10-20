package com.wm.notification.config;

import com.africastalking.AfricasTalking;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AfricaTalkingConfig {

    @Value("${africastalking.username}")
    private String username;

    @Value("${africastalking.apiKey}")
    private String apiKey;

    @Bean
    public AfricasTalking africasTalking() {
        return new AfricasTalking();
    }
}