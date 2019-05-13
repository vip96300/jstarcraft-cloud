package com.jstarcraft.cloud.registration.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaServer
@PropertySource(value = { "classpath:application.yml" })
@SpringBootApplication
public class MockEureka {

    public static void main(String[] arguments) {
        SpringApplication.run(MockEureka.class, arguments);
    }

}
