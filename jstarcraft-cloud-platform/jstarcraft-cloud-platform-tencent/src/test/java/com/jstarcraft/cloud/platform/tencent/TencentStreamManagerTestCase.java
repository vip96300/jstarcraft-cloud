package com.jstarcraft.cloud.platform.tencent;

import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.DeleteBucketRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Huang Hong Fei
 * @createAt 2021/7/22
 * @description
 */
public class TencentStreamManagerTestCase {

    /**
     * 规范:{bucketName}-{appid}
     */
    private static final String storage = "default-1306604551";

    private static final String ACCESS_KEY="AKIDf9RSDhuqVt0qPhiSw83h4AzWmlTt2uVq";

    private static final String SECRET_KEY="5vTkkTw82f15sNcaHhqiJdgTzDAsJSKv";

    private COSClient client;

    @Before
    public void before() {
        COSCredentials credentials=new BasicCOSCredentials(ACCESS_KEY,SECRET_KEY);
        //COSCredentials credentials=new AnonymousCOSCredentials();
        ClientConfig config=new ClientConfig();
        config.setCosSigner(new COSSigner());
        config.setRegion(new Region("ap-chongqing"));
        client=new COSClient(credentials,config);
        //创建bucket
        CreateBucketRequest createBucketRequest=new CreateBucketRequest(storage);
        //共有
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
        client.createBucket(createBucketRequest);
    }

    @After
    public void after() {
        DeleteBucketRequest deleteBucketRequest=new DeleteBucketRequest(storage);
        client.deleteBucket(deleteBucketRequest);
    }

    protected StreamManager getStreamManager() {
        TencentStreamManager manager = new TencentStreamManager(storage,client);
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
