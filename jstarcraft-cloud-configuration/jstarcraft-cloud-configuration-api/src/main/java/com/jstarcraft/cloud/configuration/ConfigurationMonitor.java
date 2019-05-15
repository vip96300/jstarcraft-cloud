package com.jstarcraft.cloud.configuration;

/**
 * 配置监控器
 * 
 * @author Birdy
 *
 */
public interface ConfigurationMonitor {

    /**
     * 变更
     * 
     * @param configuration
     * @param name
     */
    void change(Configuration configuration, String name);

}
