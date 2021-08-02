package com.jstarcraft.cloud.platform.kingsoft;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.ksyun.ks3.service.Ks3;

public class KingsoftStreamManager extends CloudStreamManager {

    private Ks3 ks3;

    public KingsoftStreamManager(String storage, Ks3 ks3) {
        super(storage);
        this.ks3 = ks3;
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
