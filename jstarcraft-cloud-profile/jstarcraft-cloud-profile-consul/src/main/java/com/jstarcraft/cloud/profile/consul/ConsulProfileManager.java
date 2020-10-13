package com.jstarcraft.cloud.profile.consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;

/**
 * Consul配置管理器
 * 
 * @author Birdy
 *
 */
public class ConsulProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ConsulProfileManager.class);

    private ConsulClient consul;

    private String format;

    public ConsulProfileManager(ConsulClient consul, String format) {
        this.consul = consul;
        this.format = format;
    }

    @Override
    public Option getOption(String name) {
        Response<GetValue> response = consul.getKVValue(name, QueryParams.DEFAULT);
        GetValue keyValue = response.getValue();
        String content = keyValue.getDecodedValue();
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
