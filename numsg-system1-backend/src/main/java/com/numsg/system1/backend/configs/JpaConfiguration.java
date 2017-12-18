package com.numsg.system1.backend.configs;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by numsg on 2017/3/3.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.numsg.system1")
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.numsg.system1"})
public class JpaConfiguration {
}
