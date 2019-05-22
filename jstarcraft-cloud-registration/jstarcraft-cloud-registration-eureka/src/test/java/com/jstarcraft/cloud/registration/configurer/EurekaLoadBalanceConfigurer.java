package com.jstarcraft.cloud.registration.configurer;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jstarcraft.cloud.registration.eureka.EurekaServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

@Configuration
@AutoConfigureAfter(EurekaRibbonClientConfiguration.class)
public class EurekaLoadBalanceConfigurer {

    @Value("${ribbon.eureka.approximateZoneFromHostname:false}")
    private boolean approximateZoneFromHostname = false;

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> provider) {
        ServerList<DiscoveryEnabledServer> serverList = new DiscoveryEnabledNIWSServerList(config, provider);
        serverList = new DomainExtractingServerList(serverList, config, this.approximateZoneFromHostname);
        EurekaServerManager manager = new EurekaServerManager(config, serverList);
        return manager;
    }

}