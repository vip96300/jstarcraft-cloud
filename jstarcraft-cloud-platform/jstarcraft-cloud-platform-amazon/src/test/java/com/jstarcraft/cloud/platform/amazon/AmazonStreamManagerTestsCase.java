package com.jstarcraft.cloud.platform.amazon;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.common.collect.Lists;
import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;

import io.findify.s3mock.S3Mock;

public class AmazonStreamManagerTestsCase {

    private static final String storage = "amazon";

    private static S3Mock server;

    private static AmazonS3 client;

    @BeforeClass
    public static void before() {
        server = S3Mock.create(8080);
        server.start();
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.withPathStyleAccessEnabled(true);
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setSignerOverride("S3SignerType");
        configuration.setProtocol(Protocol.HTTP);
        builder.withClientConfiguration(configuration);
        EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:8080", Regions.CA_CENTRAL_1.getName());
        builder.withEndpointConfiguration(endpoint);
        builder.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()));
        client = builder.build();
        client.createBucket(storage);
    }

    @AfterClass
    public static void after() {
        client.deleteBucket(storage);
        client.shutdown();
        server.stop();
        server.shutdown();
    }

    protected StreamManager getStreamManager() {
        AmazonStreamManager manager = new AmazonStreamManager(storage, client);
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
