package com.jstarcraft.cloud.platform.backblaze;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.jstarcraft.cloud.platform.StorageException;
import com.jstarcraft.cloud.platform.StorageMetadata;
import com.jstarcraft.cloud.platform.StorageResource;
import com.jstarcraft.cloud.platform.StorageService;

/**
 * 仓库服务
 * 
 * <pre>
 * https://github.com/Backblaze/b2-sdk-java/tree/master/samples/src/main/java/com/backblaze/b2/sample
 * </pre>
 * 
 * @author Birdy
 *
 */
public class BackblazeStorageService implements StorageService {

    private B2StorageClient b2;

    public BackblazeStorageService(B2StorageClient b2) {
        this.b2 = b2;
    }

    @Override
    public void createStorage(String name) {
        try {
            b2.deleteBucket(name);
        } catch (B2Exception exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public void deleteStorage(String name) {
        try {
            b2.deleteBucket(name);
        } catch (B2Exception exception) {
            throw new StorageException(exception);
        }
    }

    private B2FileVersion getBackblazeMetadata(StorageMetadata metadata) {
        // TODO
        return null;
    }

    private StorageMetadata getStorageMetadata(B2FileVersion metadata) {
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
