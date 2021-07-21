package com.jstarcraft.cloud.platform.amazon;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.jstarcraft.cloud.platform.CloudStreamManager;
import org.apache.commons.collections.iterators.CollatingIterator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AmazonStreamManager extends CloudStreamManager {

    private static final String storage="amazon";

    //private static final String OSS_URL="http://s3.amazonaws.com";
    private static final String OSS_URL="http://localhost:8080";

    private BasicAWSCredentials credentials;

    private ClientConfiguration configuration;

    private String bucketName;

    public AmazonStreamManager(String accessKey,String secretKey,String bucketName){
        super(storage);
        this.credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.configuration = new ClientConfiguration();
        this.bucketName=bucketName;
        //凭证验证方式
        this.configuration.setSignerOverride("S3SignerType");
        //访问协议
        this.configuration.setProtocol(Protocol.HTTP);
    }

    public AmazonStreamManager(BasicAWSCredentials credentials,ClientConfiguration configuration,String bucketName) {
        super(storage);
        this.credentials = credentials;
        this.configuration=configuration;
        this.bucketName=bucketName;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        ObjectMetadata metadata=new ObjectMetadata();
        PutObjectRequest request=new PutObjectRequest(bucketName,path,stream,metadata);
        this.getClient().putObject(request);
    }

    @Override
    public void waiveResource(String path) {
        DeleteObjectRequest request=new DeleteObjectRequest(bucketName,path);
        this.getClient().deleteObject(request);
    }

    @Override
    public boolean haveResource(String path) {
        GetObjectRequest request=new GetObjectRequest(bucketName,path);
        S3Object object=null;
        try{
            object= this.getClient().getObject(request);
        }catch (AmazonS3Exception e){
            e.printStackTrace();
            return false;
        }
        return object!=null;
    }

    @Override
    public InputStream retrieveResource(String path) {
        GetObjectRequest request=new GetObjectRequest(bucketName,path);
        S3Object object=null;
        try{
            object= this.getClient().getObject(request);
            if(object==null){
                return null;
            }
        }catch (AmazonS3Exception e){
            e.printStackTrace();
            return null;
        }
        return object.getObjectContent().getDelegateStream();
    }

    @Override
    public Iterator<String> iterateResources(String path) {
        ListObjectsRequest request=new ListObjectsRequest(bucketName,path,null,null,Integer.MAX_VALUE);
        Iterator<S3ObjectSummary> objectSummaryList=this.getClient().listObjects(request).getObjectSummaries().listIterator();
        List<String> keys=new ArrayList<>();
        while (objectSummaryList.hasNext()){
            String key=objectSummaryList.next().getKey();
            keys.add(key);
        }
        return keys.iterator();
    }

    /**
     * 支持创建多个实例，多配置，多桶
     */
    private static volatile Map<String,AmazonS3> INSTANCE_POOL=new ConcurrentHashMap<>();

    /**
     * 获取amazon oss客户端工具
     * @return
     */
    private synchronized AmazonS3 getClient(){
        String key=this.toString();
        AmazonS3 client=INSTANCE_POOL.get(key);
        if(client==null){
            synchronized (this){
                //设置要用于请求的端点配置（服务端点和签名区域）
                AwsClientBuilder.EndpointConfiguration endpointConfiguration=new AwsClientBuilder
                        .EndpointConfiguration(OSS_URL,Regions.CN_NORTH_1.toString());
                client=AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withClientConfiguration(configuration)
                        .withEndpointConfiguration(endpointConfiguration)
                        .withPathStyleAccessEnabled(true)
                        .build();
                //创建桶(已存在会忽略)
                CreateBucketRequest createBucketRequest=new CreateBucketRequest(bucketName);
                client.createBucket(createBucketRequest);
                //放入实例池
                INSTANCE_POOL.put(key,client);
            }
        }
        return client;
    }
}
