package com.jstarcraft.cloud.registration.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import com.jstarcraft.cloud.registration.configurer.EurekaLoadBalanceConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "demo", configuration = EurekaLoadBalanceConfigurer.class)
public class MockApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(MockApplication.class, arguments);
    }

}
