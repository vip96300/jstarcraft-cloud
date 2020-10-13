package com.jstarcraft.cloud.profile.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.utility.StringUtility;

public class ZooKeeperProfileManagerTestCase {

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

    @Test
    public void test() throws Exception {
        String path = "/group";
        String name = "jstarcraft";
        byte[] data = "race=random".getBytes(StringUtility.CHARSET);
        zookeeper.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "/" + name, data);
        ZooKeeperProfileManager manager = new ZooKeeperProfileManager(zookeeper, "properties", path);
        Option configurator = manager.getOption("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        zookeeper.delete().forPath(path + "/" + name);
    }

}
