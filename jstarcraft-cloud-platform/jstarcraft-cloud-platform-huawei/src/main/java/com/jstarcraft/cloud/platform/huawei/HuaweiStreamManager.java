package com.jstarcraft.cloud.platform.huawei;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObsObject;
import com.obs.services.model.fs.NewFileRequest;

public class HuaweiStreamManager extends CloudStreamManager {

    private ObsClient obs;

    public HuaweiStreamManager(String storage, ObsClient obs) {
        super(storage);
        this.obs = obs;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        obs.putObject(storage,path,stream);
    }

    @Override
    public void waiveResource(String path) {
        obs.deleteObject(storage,path);
    }

    @Override
    public boolean haveResource(String path) {
        return obs.doesObjectExist(storage,path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        ObsObject object= obs.getObject(storage,path);
        if(object==null){
            return null;
        }
        return object.getObjectContent();
    }

    private class HuaweiStreamIterator implements Iterator<String> {

        private Iterator<ObsObject> iterator;

        private HuaweiStreamIterator(Iterator<ObsObject> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            ObsObject object = iterator.next();
            return object.getObjectKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        ObjectListing listing= obs.listObjects(path);
        return new HuaweiStreamIterator(listing.getObjects().iterator());
    }

    @Override
    public long getUpdatedAt(String path) {
        ObsObject object= obs.getObject(storage,path);
        if(object==null){
            return 0;
        }
        return object.getMetadata().getLastModified().getTime();
    }

}
