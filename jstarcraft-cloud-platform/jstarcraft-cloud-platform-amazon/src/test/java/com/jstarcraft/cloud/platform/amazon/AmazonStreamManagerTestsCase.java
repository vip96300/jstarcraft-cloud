package com.jstarcraft.cloud.platform.amazon;

import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;
import io.findify.s3mock.S3Mock;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Huang Hong Fei
 * @createAt 2021/7/20
 * @description
 */
public class AmazonStreamManagerTestsCase {

    private static final String accessKey = "1";

    private static final String secretKey = "1";

    private static final String bucketName = "test";

    protected StreamManager getStreamManager() {
        AmazonStreamManager manager = new AmazonStreamManager(accessKey, secretKey, bucketName);
        return manager;
    }

    @Before
    public void setUp() {
        S3Mock mockServer = S3Mock.create(8080);
        mockServer.start();
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
}
