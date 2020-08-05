package com.jstarcraft.cloud.registration.configure;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperRibbonClientConfiguration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServerList;
import org.springframework.cloud.zookeeper.discovery.dependency.ZookeeperDependencies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jstarcraft.cloud.registration.zookeeper.ZooKeeperServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

@Configuration
@AutoConfigureAfter(ZookeeperRibbonClientConfiguration.class)
// 此类是为了演示如何覆盖Spring Cloud的自动装配
public class ZooKeeperLoadBalanceConfigurer {

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, ZookeeperDependencies dependencies, ServiceDiscovery<ZookeeperInstance> discovery) {
        ZookeeperServerList serverList = new ZookeeperServerList(discovery);
        serverList.initFromDependencies(config, dependencies);
        ZooKeeperServerManager manager = new ZooKeeperServerManager(config, serverList);
        return manager;
    }
    
    @Bean
    public ZookeeperDependencies zookeeperDependencies() {
        return new ZookeeperDependencies();
    }

}