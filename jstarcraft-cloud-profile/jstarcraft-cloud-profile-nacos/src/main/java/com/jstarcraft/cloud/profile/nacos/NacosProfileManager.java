package com.jstarcraft.cloud.profile.nacos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;

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
    public Option getOption(String name) {
        try {
            String content = nacos.getConfig(name, "group", 1000L);
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
