package com.jstarcraft.cloud.platform.tencent;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class TencentStorageService implements StorageService {

    private COSClient cos;

    public TencentStorageService(COSClient oss) {
        this.cos = oss;
    }

    @Override
    public void createStorage(String name) {
        cos.createBucket(name);
    }

    @Override
    public void deleteStorage(String name) {
        cos.deleteBucket(name);
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
        cos.putObject(storage, key, stream, metadata);
    }

    @Override
    public void waiveResource(String storage, String key) {
        cos.deleteObject(storage, key);
    }

    @Override
    public boolean haveResource(String storage, String key) {
        return cos.doesObjectExist(storage, key);
    }

    @Override
    public Iterator<StorageResource> iterateResources(String storage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        COSObject object = cos.getObject(storage, key);
        StorageMetadata metadata = getStorageMetadata(object.getObjectMetadata());
        InputStream stream = object.getObjectContent();
        StorageResource resource = new StorageResource(metadata, stream);
        return resource;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        StorageMetadata metadata = getStorageMetadata(cos.getObjectMetadata(storage, key));
        return metadata;
    }

}
