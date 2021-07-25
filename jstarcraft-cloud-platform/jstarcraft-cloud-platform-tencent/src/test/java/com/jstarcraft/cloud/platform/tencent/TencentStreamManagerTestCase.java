package com.jstarcraft.cloud.platform.tencent;

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

import com.google.common.collect.Lists;
import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.DeleteBucketRequest;
import com.qcloud.cos.region.Region;

/**
 * Huang Hong Fei
 * 
 * @author Birdy
 *
 */
public class TencentStreamManagerTestCase {

    /** {bucketName}-{appid} */
    private static String storage;

    private static String accessKey;

    private static String secretKey;

    private static String region;

    static {
        try {
            Properties keyValues = new Properties();
            keyValues.load(new FileInputStream("cos.properties"));
            storage = keyValues.getProperty("storage");
            accessKey = keyValues.getProperty("access_key");
            secretKey = keyValues.getProperty("secret_key");
            region = keyValues.getProperty("region");
        } catch (Exception exception) {
        }
    }

    private static COSClient cos;

    @BeforeClass
    public static void before() throws Exception {
        COSCredentials credentials = new BasicCOSCredentials(accessKey, secretKey);
        ClientConfig configuration = new ClientConfig();
        configuration.setCosSigner(new COSSigner());
        configuration.setRegion(new Region(region));
        cos = new COSClient(credentials, configuration);

        CreateBucketRequest createBucketRequest = new CreateBucketRequest(storage);
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
        cos.createBucket(createBucketRequest);
    }

    @AfterClass
    public static void after() {
        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(storage);
        cos.deleteBucket(deleteBucketRequest);
    }

    protected StreamManager getStreamManager() {
        TencentStreamManager manager = new TencentStreamManager(storage, cos);
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
