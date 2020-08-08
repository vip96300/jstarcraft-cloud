package com.jstarcraft.cloud.governance;

import java.util.List;

/**
 * 治理管理器
 * 
 * @author Birdy
 *
 */
public interface GovernanceManager {

    /**
     * 注册实例
     * 
     * @param instance
     */
    void registerInstance(GovernanceInstance instance);

    /**
     * 注销实例
     * 
     * @param instance
     */
    void deregisterInstance(GovernanceInstance instance);

    /**
     * 发现实例
     * 
     * @param name
     * @return
     */
    List<GovernanceInstance> discoverInstances(String name);

}
