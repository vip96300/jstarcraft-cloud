package com.jstarcraft.cloud.platform.upyun;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.upyun.RestManager;

public class UpyunStreamManager extends CloudStreamManager {

    private RestManager uss;

    public UpyunStreamManager(String storage, String key, String secret) {
        super(storage);
        this.uss = new RestManager(storage, key, secret);
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
