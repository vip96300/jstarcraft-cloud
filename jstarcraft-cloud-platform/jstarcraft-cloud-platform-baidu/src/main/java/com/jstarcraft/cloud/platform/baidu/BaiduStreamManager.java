package com.jstarcraft.cloud.platform.baidu;

import java.io.InputStream;
import java.util.Iterator;

import com.baidubce.BceServiceException;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.jstarcraft.cloud.platform.CloudStreamManager;

public class BaiduStreamManager extends CloudStreamManager {

    private BosClient bos;

    public BaiduStreamManager(String storage, BosClient bos) {
        super(storage);
        this.bos = bos;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        bos.putObject(storage, path, stream);
    }

    @Override
    public void waiveResource(String path) {
        try {
            bos.deleteObject(storage, path);
        } catch (BceServiceException exception) {
        }
    }

    @Override
    public boolean haveResource(String path) {
        return bos.doesObjectExist(storage, path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        try {
            BosObject object = bos.getObject(storage, path);
            if (object != null) {
                return object.getObjectContent();
            } else {
                return null;
            }
        } catch (BceServiceException exception) {
            return null;
        }
    }

    private class BaiduStreamIterator implements Iterator<String> {

        private Iterator<BosObjectSummary> iterator;

        private BaiduStreamIterator(Iterator<BosObjectSummary> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            BosObjectSummary object = iterator.next();
            return object.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        Iterator<BosObjectSummary> iterator = bos.listObjects(storage, path).getContents().iterator();
        return new BaiduStreamIterator(iterator);
    }
    
    @Override
    public long getUpdatedAt(String path) {
        ObjectMetadata metadata = bos.getObjectMetadata(storage, path);
        return metadata.getLastModified().getTime();
    }

}
