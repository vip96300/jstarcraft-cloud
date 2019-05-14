package com.jstarcraft.cloud.configuration.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.PropertySource;

@EnableConfigServer
@PropertySource(value = { "classpath:application.yml" })
@SpringBootApplication
public class MockConfig {

    @Autowired
    private ConfigProperties properties;

    @PostConstruct
    void postConstruct() {

    }

    public static void main(String[] arguments) {
        SpringApplication.run(MockConfig.class, arguments);
    }

}
