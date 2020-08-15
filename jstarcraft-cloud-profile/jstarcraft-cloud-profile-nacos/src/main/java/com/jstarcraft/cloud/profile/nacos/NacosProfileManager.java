package com.jstarcraft.cloud.profile.nacos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.config.ConfigService;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;

/**
 * Nacos配置管理器
 * 
 * @author Birdy
 *
 */
public class NacosProfileManager implements ProfileManager {
    
    private static final Logger logger = LoggerFactory.getLogger(NacosProfileManager.class);

    private ConfigService nacos;
    
    @Override
    public Configurator getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
