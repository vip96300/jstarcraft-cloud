package com.jstarcraft.cloud.platform.min;

import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;

import io.minio.MinioClient;
import io.minio.messages.ObjectMetadata;

/**
 * 仓库服务
 * 
 * @author Birdy
 *
 */
public class MinStorageService implements StorageService {

    private MinioClient io;

    public MinStorageService(MinioClient io) {
        this.io = io;
    }

    @Override
    public void createStorage(String name) {
        // TODO
    }

    @Override
    public void deleteStorage(String name) {
        // TODO
    }

    private ObjectMetadata getMinMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(ObjectMetadata metadata) {
        // TODO
        return null;
    }

    @Override
    public void saveResource(String storage, String key, StorageResource resource) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void waiveResource(String storage, String key) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean haveResource(String storage, String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public StorageResource retrieveResource(String storage, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StorageMetadata retrieveMetadata(String storage, String key) {
        // TODO Auto-generated method stub
        return null;
    }

   

}
