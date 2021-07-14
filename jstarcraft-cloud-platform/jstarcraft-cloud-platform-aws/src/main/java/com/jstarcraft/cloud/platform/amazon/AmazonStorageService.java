package com.jstarcraft.cloud.platform.amazon;

import java.io.InputStream;
import java.util.Iterator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class AmazonStorageService implements StorageService {

    private AmazonS3 s3;

    public AmazonStorageService(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public void createStorage(String name) {
        s3.createBucket(name);
    }

    @Override
    public void deleteStorage(String name) {
        s3.deleteBucket(name);
    }

    private ObjectMetadata getAmazonMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(ObjectMetadata metadata) {
        // TODO
        return null;
    }

    @Override
    public void saveResource(String storage, String key, StorageResource resource) {
        ObjectMetadata metadata = getAmazonMetadata(resource.getMetadata());
        InputStream stream = resource.getStream();
        s3.putObject(storage, key, stream, metadata);
    }

    @Override
    public void waiveResource(String storage, String key) {
        s3.deleteObject(storage, key);
    }

    @Override
    public boolean haveResource(String storage, String key) {
        return s3.doesObjectExist(storage, key);
    }

    @Override
    public Iterator<StorageResource> iterateResources(String storage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        S3Object object = s3.getObject(storage, key);
        StorageMetadata metadata = getStorageMetadata(object.getObjectMetadata());
        InputStream stream = object.getObjectContent();
        StorageResource resource = new StorageResource(metadata, stream);
        return resource;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        StorageMetadata metadata = getStorageMetadata(s3.getObjectMetadata(storage, key));
        return metadata;
    }

}
