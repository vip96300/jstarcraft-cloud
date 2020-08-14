package com.jstarcraft.cloud.profile.nacos;

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
