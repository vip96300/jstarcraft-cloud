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
     * @param manager
     * @param name
     */
    void change(ConfigurationManager manager, String name);

}
