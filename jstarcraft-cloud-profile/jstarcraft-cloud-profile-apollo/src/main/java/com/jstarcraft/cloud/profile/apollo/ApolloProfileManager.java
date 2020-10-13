package com.jstarcraft.cloud.profile.apollo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;

/**
 * Apollo配置管理器
 * 
 * @author Birdy
 *
 */
public class ApolloProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ApolloProfileManager.class);

    private ConfigFileFormat format;

    public ApolloProfileManager(ConfigFileFormat format) {
        this.format = format;
    }

    @Override
    public Option getOption(String name) {
        ConfigFile config = ConfigService.getConfigFile(name, format);
        String content = config.getContent();
        switch (format) {
        case JSON:
            return new JsonOption(content);
        case Properties:
            return new PropertyOption(content);
        case XML:
            return new XmlOption(content);
        case YAML:
            return new YamlOption(content);
        default:
            throw new IllegalArgumentException();
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
