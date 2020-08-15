package com.jstarcraft.cloud.governance.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

/**
 * Redis治理管理器
 * 
 * @author Birdy
 *
 */
public class RedisGovernanceManager implements GovernanceManager {
    
    private static final Logger logger = LoggerFactory.getLogger(RedisGovernanceManager.class);

    @Override
    public void registerInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> discoverCategories() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        // TODO Auto-generated method stub
        return null;
    }

}
