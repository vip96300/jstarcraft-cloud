package com.jstarcraft.cloud.platform.google;

import java.io.InputStream;
import java.util.Iterator;

import com.google.api.services.storage.Storage;
import com.jstarcraft.cloud.platform.CloudStreamManager;

public class GoogleStreamManager extends CloudStreamManager {

    private Storage gcs;;

    public GoogleStreamManager(String storage, Storage gcs) {
        super(storage);
        this.gcs = gcs;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void waiveResource(String path) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean haveResource(String path) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InputStream retrieveResource(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> iterateResources(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getUpdatedAt(String path) {
        // TODO Auto-generated method stub
        return 0;
    }

    

}
