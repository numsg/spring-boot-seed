package com.numsg.system1.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by numsg on 2017/3/7.
 */
@Configuration
public class ApplicationBeanConfig {
    @Bean
    public AppInit appInitBean() {
        return new AppInit();
    }
}
