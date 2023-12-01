package com.js.CurrencyConverter.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CurrencyConvertConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
