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
     * @param name
     * @param from
     * @param to
     */
    void change(String name, Configuration from, Configuration to);

}
