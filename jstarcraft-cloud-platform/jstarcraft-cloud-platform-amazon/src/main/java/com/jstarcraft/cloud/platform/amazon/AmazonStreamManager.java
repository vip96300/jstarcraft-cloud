package com.jstarcraft.cloud.platform.amazon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.jstarcraft.cloud.platform.CloudStreamManager;

public class AmazonStreamManager extends CloudStreamManager {

    private static final String storage = "amazon";

    // private static final String OSS_URL="http://s3.amazonaws.com";
    private static final String OSS_URL = "http://localhost:8080";

    private BasicAWSCredentials credentials;

    private ClientConfiguration configuration;

    private String bucketName;

    private AmazonS3 s3;

    public AmazonStreamManager(String accessKey, String secretKey, String bucketName) {
        super(storage);
        this.credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.configuration = new ClientConfiguration();
        this.bucketName = bucketName;
        // 凭证验证方式
        this.configuration.setSignerOverride("S3SignerType");
        // 访问协议
        this.configuration.setProtocol(Protocol.HTTP);
        // 设置要用于请求的端点配置（服务端点和签名区域）
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(OSS_URL, Regions.CN_NORTH_1.toString());
        s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(configuration).withEndpointConfiguration(endpointConfiguration).withPathStyleAccessEnabled(true).build();
        // 创建桶(已存在会忽略)
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        s3.createBucket(createBucketRequest);
    }

    public AmazonStreamManager(BasicAWSCredentials credentials, ClientConfiguration configuration, String bucketName) {
        super(storage);
        this.credentials = credentials;
        this.configuration = configuration;
        this.bucketName = bucketName;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(bucketName, path, stream, metadata);
        s3.putObject(request);
    }

    @Override
    public void waiveResource(String path) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, path);
        s3.deleteObject(request);
    }

    @Override
    public boolean haveResource(String path) {
        return s3.doesObjectExist(bucketName, path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        GetObjectRequest request = new GetObjectRequest(bucketName, path);
        try {
            S3Object object = s3.getObject(request);
            return object.getObjectContent().getDelegateStream();
        } catch (AmazonS3Exception exception) {
            return null;
        }
    }

    @Override
    public Iterator<String> iterateResources(String path) {
        ListObjectsRequest request = new ListObjectsRequest(bucketName, path, null, null, Integer.MAX_VALUE);
        Iterator<S3ObjectSummary> objectSummaryList = s3.listObjects(request).getObjectSummaries().listIterator();
        List<String> keys = new ArrayList<>();
        while (objectSummaryList.hasNext()) {
            String key = objectSummaryList.next().getKey();
            keys.add(key);
        }
        return keys.iterator();
    }

}
