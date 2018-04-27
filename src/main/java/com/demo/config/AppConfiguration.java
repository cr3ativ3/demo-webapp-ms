package com.demo.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.demo"})
public class AppConfiguration {

    @Bean
    public Executor getExecutor() {
        return Executors.newCachedThreadPool();
    }
}
