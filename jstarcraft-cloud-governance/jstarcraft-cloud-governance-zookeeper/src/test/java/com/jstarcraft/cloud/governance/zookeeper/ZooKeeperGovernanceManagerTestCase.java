package com.jstarcraft.cloud.governance.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

public class ZooKeeperGovernanceManagerTestCase {

    private static CuratorFramework zookeeper;

    @BeforeClass
    public static void start() throws Exception {
        zookeeper = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(new RetryOneTime(2000)).build();
        zookeeper.start();
    }

    @AfterClass
    public static void stop() throws Exception {
        zookeeper.close();
    }

    protected GovernanceManager getManager() {
        String path = "/path";
        ZooKeeperGovernanceManager manager = new ZooKeeperGovernanceManager(zookeeper, path);
        return manager;
    }

    @Test
    public void testDiscover() throws Exception {
        GovernanceManager manager = getManager();
        String[] ids = { "protoss", "terran", "zerg" };
        String name = "JSTARCRAFT";
        String host = "localhost";
        int port = 1000;
        Map<String, String> metadata = new HashMap<>();

        List<GovernanceInstance> instances = new ArrayList<>(ids.length);
        for (String id : ids) {
            GovernanceInstance instance = new DefaultGovernanceInstance(id, name, host, port, metadata);
            manager.registerInstance(instance);
            instances.add(instance);
            port++;
        }
        while (manager.discoverInstances(name).size() == 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(3, manager.discoverInstances(name).size());

        for (GovernanceInstance instance : instances) {
            manager.deregisterInstance(instance);
        }
        while (manager.discoverInstances(name).size() != 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(0, manager.discoverInstances(name).size());
    }

}
