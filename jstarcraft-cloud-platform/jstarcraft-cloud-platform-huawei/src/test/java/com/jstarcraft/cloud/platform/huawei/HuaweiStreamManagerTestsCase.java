package com.jstarcraft.cloud.platform.huawei;

import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class HuaweiStreamManagerTestsCase {

    private static String storage;

    private static String accessKey;

    private static String secretKey;

    private static String endpoint;

    static {
        try {
            Properties keyValues = new Properties();
            keyValues.load(new FileInputStream("obs.properties"));
            storage = keyValues.getProperty("storage");
            accessKey = keyValues.getProperty("access_key");
            secretKey = keyValues.getProperty("secret_key");
            endpoint = keyValues.getProperty("endpoint");
        } catch (Exception exception) {
        }
    }

    private static ObsClient obs;

    @BeforeClass
    public static void before() {
        obs=new ObsClient(accessKey,secretKey,endpoint);
        obs.createBucket(storage);
    }

    @AfterClass
    public static void after() {
        obs.deleteBucket(storage);
        try {
            obs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected StreamManager getStreamManager() {
        HuaweiStreamManager manager = new HuaweiStreamManager(storage, obs);
        return manager;
    }

    @Test
    public void testHaveResource() throws Exception {
        StreamManager manager = getStreamManager();
        String path = "left/middle/right.txt";

        {
            Assert.assertFalse(manager.haveResource(path));
            InputStream stream = manager.retrieveResource(path);
            Assert.assertNull(stream);
        }

        try (InputStream stream = new ByteArrayInputStream(path.getBytes(StringUtility.CHARSET))) {
            manager.saveResource(path, stream);
            Assert.assertTrue(manager.haveResource(path));
        }

        try (InputStream stream = manager.retrieveResource(path)) {
            String content = IOUtils.readLines(stream, StringUtility.CHARSET).get(0);
            Assert.assertEquals(path, content);
        }

        manager.waiveResource(path);
        Assert.assertFalse(manager.haveResource(path));
    }
    
    @Test
    public void testIterateResources() throws Exception {
        StreamManager manager = getStreamManager();
        String[] types = { "protoss", "terran", "zerg" };
        for (String type : types) {
            String path = "jstarcraft/" + type + ".txt";
            // 保存资源
            try (InputStream stream = new ByteArrayInputStream(type.getBytes(StringUtility.CHARSET))) {
                manager.saveResource(path, stream);
                Assert.assertTrue(manager.haveResource(path));
            }
        }
        for (String type : types) {
            String path = "jstarcraft/" + type + "/" + type + ".txt";
            // 保存资源
            try (InputStream stream = new ByteArrayInputStream(type.getBytes(StringUtility.CHARSET))) {
                manager.saveResource(path, stream);
                Assert.assertTrue(manager.haveResource(path));
            }
        }

        Assert.assertFalse(manager.haveResource("jstarcraft/"));
        Assert.assertFalse(manager.haveResource("jstarcraft/protoss/"));
        Assert.assertFalse(manager.haveResource("jstarcraft/terran/"));
        Assert.assertFalse(manager.haveResource("jstarcraft/zerg/"));

        List<String> resources = Lists.newArrayList(manager.iterateResources("jstarcraft/"));
        Assert.assertEquals(6, resources.size());

        for (String type : types) {
            String path = "jstarcraft/" + type + "/" + type + ".txt";
            // 获取资源
            try (InputStream stream = manager.retrieveResource(path)) {
                String content = IOUtils.readLines(stream, StringUtility.CHARSET).get(0);
                Assert.assertEquals(type, content);
            }
        }

        for (String type : types) {
            String path = "jstarcraft/" + type + "/" + type + ".txt";
            // 废弃资源
            manager.waiveResource(path);
            Assert.assertFalse(manager.haveResource(path));
        }

        resources = Lists.newArrayList(manager.iterateResources("jstarcraft/"));
        Assert.assertEquals(3, resources.size());

        // 废弃资源
        manager.waiveResource("jstarcraft/");
        resources = Lists.newArrayList(manager.iterateResources("jstarcraft/"));
        Assert.assertEquals(3, resources.size());

        for (String type : types) {
            String path = "jstarcraft/" + type + ".txt";
            // 废弃资源
            manager.waiveResource(path);
            Assert.assertFalse(manager.haveResource(path));
        }

        resources = Lists.newArrayList(manager.iterateResources("jstarcraft/"));
        Assert.assertEquals(0, resources.size());
    }
    
}
