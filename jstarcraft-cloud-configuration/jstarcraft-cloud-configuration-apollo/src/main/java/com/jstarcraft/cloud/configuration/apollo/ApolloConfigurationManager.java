package com.jstarcraft.cloud.configuration.apollo;

import java.util.HashMap;
import java.util.Map;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.utility.Configuration;

/**
 * Apollo配置管理器
 * 
 * @author Birdy
 *
 */
public class ApolloConfigurationManager implements ConfigurationManager {

    private Config apollo;

    private Map<ConfigurationMonitor, ConfigChangeListener> monitors = new HashMap<>();

    @Override
    public Configuration getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized void registerMonitor(ConfigurationMonitor monitor) {
        ConfigChangeListener listener = new ConfigChangeListener() {

            @Override
            public void onChange(ConfigChangeEvent event) {
                String name = event.getNamespace();
                // TODO 此处需要重构
                monitor.change(name, null, null);
            }

        };
        if (monitors.putIfAbsent(monitor, listener) == null) {
            apollo.addChangeListener(listener);
        }
    }

    @Override
    public synchronized void unregisterMonitor(ConfigurationMonitor monitor) {
        ConfigChangeListener listener = monitors.remove(monitor);
        if (listener != null) {
            apollo.removeChangeListener(listener);
        }
    }

}
