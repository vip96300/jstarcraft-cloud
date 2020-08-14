package com.jstarcraft.cloud.configuration.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.PropertySource;

@EnableConfigServer
@PropertySource(value = { "classpath:application.properties" })
@SpringBootApplication
public class MockConfig {

    public static void main(String[] arguments) {
        SpringApplication.run(MockConfig.class, arguments);
    }

}
