package com.jstarcraft.cloud.platform.baidu;

import java.io.InputStream;

import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class BaiduStorageService implements StorageService {

    private BosClient bos;

    public BaiduStorageService(BosClient bos) {
        this.bos = bos;
    }

    @Override
    public void createStorage(String name) {
        bos.createBucket(name);
    }

    @Override
    public void deleteStorage(String name) {
        bos.deleteBucket(name);
    }

    private ObjectMetadata getBaiduMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(ObjectMetadata metadata) {
        // TODO
        return null;
    }

    @Override
    public void saveResource(String storage, String key, StorageResource resource) {
        ObjectMetadata metadata = getBaiduMetadata(resource.getMetadata());
        InputStream stream = resource.getStream();
        bos.putObject(storage, key, stream, metadata);
    }

    @Override
    public void waiveResource(String storage, String key) {
        bos.deleteObject(storage, key);
    }

    @Override
    public boolean haveResource(String storage, String key) {
        return bos.doesObjectExist(storage, key);
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        BosObject object = bos.getObject(storage, key);
        StorageMetadata metadata = getStorageMetadata(object.getObjectMetadata());
        InputStream stream = object.getObjectContent();
        StorageResource resource = new StorageResource(metadata, stream);
        return resource;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        StorageMetadata metadata = getStorageMetadata(bos.getObjectMetadata(storage, key));
        return metadata;
    }

}
