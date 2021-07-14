package com.jstarcraft.cloud.platform.huawei;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class HuaweiStorageService implements StorageService {

    private ObsClient obs;

    public HuaweiStorageService(ObsClient obs) {
        this.obs = obs;
    }

    @Override
    public void createStorage(String name) {
        obs.createBucket(name);
    }

    @Override
    public void deleteStorage(String name) {
        obs.deleteBucket(name);
    }

    private ObjectMetadata getHuaweiMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(ObjectMetadata metadata) {
        // TODO
        return null;
    }

    @Override
    public void saveResource(String storage, String key, StorageResource resource) {
        ObjectMetadata metadata = getHuaweiMetadata(resource.getMetadata());
        InputStream stream = resource.getStream();
        obs.putObject(storage, key, stream, metadata);
    }

    @Override
    public void waiveResource(String storage, String key) {
        obs.deleteObject(storage, key);
    }

    @Override
    public boolean haveResource(String storage, String key) {
        return obs.doesObjectExist(storage, key);
    }

    @Override
    public Iterator<StorageResource> iterateResources(String storage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        ObsObject object = obs.getObject(storage, key);
        StorageMetadata metadata = getStorageMetadata(object.getMetadata());
        InputStream stream = object.getObjectContent();
        StorageResource resource = new StorageResource(metadata, stream);
        return resource;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        StorageMetadata metadata = getStorageMetadata(obs.getObjectMetadata(storage, key));
        return metadata;
    }

}
