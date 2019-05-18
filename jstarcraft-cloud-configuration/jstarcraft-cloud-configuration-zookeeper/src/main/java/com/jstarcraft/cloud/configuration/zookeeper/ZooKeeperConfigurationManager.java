package com.jstarcraft.cloud.configuration.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.jstarcraft.cloud.configuration.Configuration;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;

/**
 * ZooKeeper配置管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperConfigurationManager implements ConfigurationManager {

    private String path;

    private CuratorFramework curator;

    @Override
    public Configuration getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerMonitor(ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
