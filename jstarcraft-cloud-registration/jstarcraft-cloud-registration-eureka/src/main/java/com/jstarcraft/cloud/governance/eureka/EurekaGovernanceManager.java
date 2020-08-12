package com.jstarcraft.cloud.governance.eureka;

import java.util.List;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

/**
 * Consul治理管理器
 * 
 * @author Birdy
 *
 */
public class EurekaGovernanceManager implements GovernanceManager {

    private EurekaClient eureka;

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
        InstanceInfo serverInfo = eureka.getNextServerFromEureka(category, false);
        return null;
    }

}
