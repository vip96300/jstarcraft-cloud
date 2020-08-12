package com.jstarcraft.cloud.governance.kubernetes;

import java.util.List;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

/**
 * Consul治理管理器
 * 
 * @author Birdy
 *
 */
public class KubernetesGovernanceManager implements GovernanceManager {

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
