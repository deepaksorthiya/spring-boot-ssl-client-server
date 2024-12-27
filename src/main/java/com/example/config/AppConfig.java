package com.example.config;

import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient(RestClient.Builder restClientBuilder, RestClientSsl ssl) {
        return restClientBuilder.apply(ssl.fromBundle("client")).build();
    }

}
