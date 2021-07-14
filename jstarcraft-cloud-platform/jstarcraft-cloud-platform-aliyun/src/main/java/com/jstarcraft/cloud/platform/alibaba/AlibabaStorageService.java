package com.jstarcraft.cloud.platform.alibaba;

import java.io.InputStream;
import java.util.Iterator;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class AlibabaStorageService implements StorageService {

    private OSSClient oss;

    public AlibabaStorageService(OSSClient oss) {
        this.oss = oss;
    }

    @Override
    public void createStorage(String name) {
        oss.createBucket(name);
    }

    @Override
    public void deleteStorage(String name) {
        oss.deleteBucket(name);
    }

    private ObjectMetadata getAliyunMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(ObjectMetadata metadata) {
        // TODO
        return null;
    }

    @Override
    public void saveResource(String storage, String key, StorageResource resource) {
        ObjectMetadata metadata = getAliyunMetadata(resource.getMetadata());
        InputStream stream = resource.getStream();
        oss.putObject(storage, key, stream, metadata);
    }

    @Override
    public void waiveResource(String storage, String key) {
        oss.deleteObject(storage, key);
    }

    @Override
    public boolean haveResource(String storage, String key) {
        return oss.doesObjectExist(storage, key);
    }

    @Override
    public Iterator<StorageResource> iterateResources(String storage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        OSSObject object = oss.getObject(storage, key);
        StorageMetadata metadata = getStorageMetadata(object.getObjectMetadata());
        InputStream stream = object.getObjectContent();
        StorageResource resource = new StorageResource(metadata, stream);
        return resource;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        StorageMetadata metadata = getStorageMetadata(oss.getObjectMetadata(storage, key));
        return metadata;
    }

}
