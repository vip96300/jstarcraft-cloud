package com.jstarcraft.cloud.profile.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;
import com.jstarcraft.core.utility.StringUtility;

/**
 * ZooKeeper配置管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperProfileManager.class);

    private CuratorFramework zookeeper;

    private String format;

    private String path;

    public ZooKeeperProfileManager(CuratorFramework zookeeper, String format, String path) {
        this.zookeeper = zookeeper;
        this.format = format;
        this.path = path;
    }

    @Override
    public Option getOption(String name) {
        try {
            byte[] data = zookeeper.getData().forPath(path + "/" + name);
            String content = new String(data, StringUtility.CHARSET);
            switch (format) {
            case "json":
                return new JsonOption(content);
            case "properties":
                return new PropertyOption(content);
            case "xml":
                return new XmlOption(content);
            case "yaml":
                return new YamlOption(content);
            }
            throw new IllegalArgumentException();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
