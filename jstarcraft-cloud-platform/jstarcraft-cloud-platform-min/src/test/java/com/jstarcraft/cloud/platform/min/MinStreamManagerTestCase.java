package com.jstarcraft.cloud.platform.min;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;

/**
 * 
 * @author Huang Hong Fei
 *
 */
public class MinStreamManagerTestCase {

    private static final String storage = "min";

    private static final String ENDPOINT = "https://play.min.io";

    private static final String ACCESS_KEY = "Q3AM3UQ867SPQQA43P2F";

    private static final String SECRET_KEY = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";

    private static MinioClient io;

    @BeforeClass
    public static void before() throws Exception {
        try {
            io = MinioClient.builder().credentials(ACCESS_KEY, SECRET_KEY).endpoint(ENDPOINT).build();
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(storage).objectLock(false).build();
            io.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void after() throws Exception {
        RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(storage).build();
        io.removeBucket(removeBucketArgs);
    }

    protected StreamManager getStreamManager() {
        MinStreamManager manager = new MinStreamManager(storage, io);
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
