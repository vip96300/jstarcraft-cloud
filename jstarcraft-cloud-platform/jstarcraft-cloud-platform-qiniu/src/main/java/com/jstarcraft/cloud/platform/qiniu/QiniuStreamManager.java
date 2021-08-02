package com.jstarcraft.cloud.platform.qiniu;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.qiniu.http.Client;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiniuStreamManager extends CloudStreamManager {

    private BucketManager bucket;

    private UploadManager upload;

    public QiniuStreamManager(String storage, Auth auth, Client kodo) {
        super(storage);
        this.bucket = new BucketManager(auth, kodo);
        this.upload = new UploadManager(kodo, null);
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
