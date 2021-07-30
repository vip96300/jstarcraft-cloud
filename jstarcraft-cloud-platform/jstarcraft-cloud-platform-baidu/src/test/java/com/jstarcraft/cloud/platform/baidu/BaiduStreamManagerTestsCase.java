package com.jstarcraft.cloud.platform.baidu;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidubce.auth.BceCredentials;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.google.common.collect.Lists;
import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;

public class BaiduStreamManagerTestsCase {

    private static String storage;

    private static String accessKey;

    private static String secretKey;

    private static String domain;

    static {
        try {
            Properties keyValues = new Properties();
            keyValues.load(new FileInputStream("bos.properties"));
            storage = keyValues.getProperty("storage");
            accessKey = keyValues.getProperty("access_key");
            secretKey = keyValues.getProperty("secret_key");
            domain = keyValues.getProperty("domain");
        } catch (Exception exception) {
        }
    }

    private static BosClient bos;

    @BeforeClass
    public static void before() {
        BceCredentials credentials = new DefaultBceCredentials(accessKey, secretKey);
        BosClientConfiguration configuration = new BosClientConfiguration();
        configuration.setCredentials(credentials);
        configuration.setEndpoint(domain);
        bos = new BosClient(configuration);
        bos.createBucket(storage);
    }

    @AfterClass
    public static void after() {
        bos.deleteBucket(storage);
        bos.shutdown();
    }

    protected StreamManager getStreamManager() {
        BaiduStreamManager manager = new BaiduStreamManager(storage, bos);
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
