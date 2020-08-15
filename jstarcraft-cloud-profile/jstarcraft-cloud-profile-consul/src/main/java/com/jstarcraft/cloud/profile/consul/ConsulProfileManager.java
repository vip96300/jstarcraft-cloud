package com.jstarcraft.cloud.profile.consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;

/**
 * Consul配置管理器
 * 
 * @author Birdy
 *
 */
public class ConsulProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ConsulProfileManager.class);

    private ConsulClient consul;
    
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
