package com.jstarcraft.cloud.configuration.redis;

import org.redisson.Redisson;

import com.jstarcraft.cloud.configuration.Configuration;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;

/**
 * Redis配置管理器
 * 
 * @author Birdy
 *
 */
public class RedisConfigurationManager implements ConfigurationManager {

    private Redisson redisson;

    @Override
    public Configuration getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerMonitor(ConfigurationMonitor monitor, boolean synchronous) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
