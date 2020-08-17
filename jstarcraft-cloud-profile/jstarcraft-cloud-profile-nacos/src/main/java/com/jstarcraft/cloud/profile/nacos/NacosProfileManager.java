package com.jstarcraft.cloud.profile.nacos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.JsonConfigurator;
import com.jstarcraft.core.common.configuration.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.XmlConfigurator;
import com.jstarcraft.core.common.configuration.YamlConfigurator;

/**
 * Nacos配置管理器
 * 
 * @author Birdy
 *
 */
public class NacosProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(NacosProfileManager.class);

    private ConfigService nacos;

    private String format;

    public NacosProfileManager(ConfigService nacos, String format) {
        this.nacos = nacos;
        this.format = format;
    }

    @Override
    public Configurator getConfiguration(String name) {
        try {
            String value = nacos.getConfig(name, "group", 1000L);
            switch (format) {
            case "json":
                return new JsonConfigurator(value);
            case "properties":
                return new PropertyConfigurator(value);
            case "xml":
                return new XmlConfigurator(value);
            case "yaml":
                return new YamlConfigurator(value);
            }
            throw new IllegalArgumentException();
        } catch (NacosException exception) {
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
