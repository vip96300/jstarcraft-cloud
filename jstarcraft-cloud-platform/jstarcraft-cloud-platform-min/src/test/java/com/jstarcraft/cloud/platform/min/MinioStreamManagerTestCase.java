package com.jstarcraft.cloud.platform.min;

import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Huang Hong Fei
 * @createAt 2021/7/22
 * @description
 */
public class MinioStreamManagerTestCase {

    private static final String storage= "935160293";

    private MinioClient client;

    private static final String ENDPOINT="https://play.min.io";

    private static final String ACCESS_KEY="Q3AM3UQ867SPQQA43P2F";

    private static final String SECRET_KEY="zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";

    @Before
    public void before() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        client= MinioClient.builder()
                .credentials(ACCESS_KEY,SECRET_KEY)
                .endpoint(ENDPOINT)
                .build();
        MakeBucketArgs makeBucketArgs=MakeBucketArgs.builder()
                .bucket(storage)
                .objectLock(false)
                .build();
        client.makeBucket(makeBucketArgs);
    }

    @After
    public void after() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        RemoveBucketArgs removeBucketArgs= RemoveBucketArgs.builder()
                .bucket(storage)
                .build();
        client.removeBucket(removeBucketArgs);
    }


    protected StreamManager getStreamManager() {
        MinioStreamManager manager = new MinioStreamManager(storage,client);
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
}
