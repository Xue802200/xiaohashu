package com.quanxiaoha.framework.jackson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanxiaoha.framework.jackson.core.JacksonConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class JacksonAutoConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new JacksonConfig().objectMapper();
    }
}
