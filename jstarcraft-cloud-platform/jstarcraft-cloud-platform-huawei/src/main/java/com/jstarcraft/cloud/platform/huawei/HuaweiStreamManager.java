package com.jstarcraft.cloud.platform.huawei;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.obs.services.ObsClient;

public class HuaweiStreamManager extends CloudStreamManager {

    private ObsClient obs;

    public HuaweiStreamManager(String storage, ObsClient obs) {
        super(storage);
        this.obs = obs;
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
