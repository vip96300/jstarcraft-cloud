package com.jstarcraft.cloud.platform.baidu;

import com.baidubce.BceServiceException;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.jstarcraft.cloud.platform.CloudStreamManager;

import java.io.InputStream;
import java.util.Iterator;

public class BaiduStreamManager extends CloudStreamManager {

    private BosClient client;

    public BaiduStreamManager(String storage, BosClient client) {
        super(storage);
        this.client = client;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        client.putObject(storage,path,stream);
    }

    @Override
    public void waiveResource(String path) {
        client.deleteObject(storage,path);
    }

    @Override
    public boolean haveResource(String path) {
        return client.doesObjectExist(storage,path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        try {
            BosObject object=client.getObject(storage,path);
            if(object!=null){
                return object.getObjectContent();
            }
        }catch (BceServiceException e){
            return null;
        }
        return null;
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
        ListObjectsResponse response=client.listObjects(storage,path);
        return new BaiduStreamIterator(response.getContents().iterator());
    }

}
