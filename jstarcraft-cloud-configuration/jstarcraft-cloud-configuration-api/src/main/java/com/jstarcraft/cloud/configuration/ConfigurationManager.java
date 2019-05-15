package com.jstarcraft.cloud.configuration;

import com.jstarcraft.core.utility.StringUtility;

/**
 * 配置管理器
 * 
 * @author Birdy
 *
 */
public interface ConfigurationManager {

    /**
     * 获取配置
     * 
     * @param name
     * @return
     */
    Configuration getConfiguration(String name);

    /**
     * 注册监控器
     * 
     * @param monitor
     * @param synchronous
     */
    void registerMonitor(ConfigurationMonitor monitor, boolean synchronous);

    /**
     * 注销监控器
     * 
     * @param monitor
     */
    void unregisterMonitor(ConfigurationMonitor monitor);

}
