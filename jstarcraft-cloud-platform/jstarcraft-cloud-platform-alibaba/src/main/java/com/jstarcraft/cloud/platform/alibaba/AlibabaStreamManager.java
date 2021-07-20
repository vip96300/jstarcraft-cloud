package com.jstarcraft.cloud.platform.alibaba;

import java.io.InputStream;
import java.util.Iterator;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.jstarcraft.cloud.platform.CloudStreamManager;

public class AlibabaStreamManager extends CloudStreamManager {

    private OSSClient oss;

    public AlibabaStreamManager(String storage, OSSClient oss) {
        super(storage);
        this.oss = oss;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        oss.putObject(storage, path, stream);
    }

    @Override
    public void waiveResource(String path) {
        oss.deleteObject(storage, path);
    }

    @Override
    public boolean haveResource(String path) {
        return oss.doesObjectExist(storage, path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        OSSObject object = oss.getObject(storage, path);
        InputStream stream = object.getObjectContent();
        return stream;
    }

    private class AlibabaStreamIterator implements Iterator<String> {

        private Iterator<OSSObjectSummary> iterator;

        private AlibabaStreamIterator(Iterator<OSSObjectSummary> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            OSSObjectSummary object = iterator.next();
            return object.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        ObjectListing objects = oss.listObjects(storage, path);
        Iterator<OSSObjectSummary> iterator = objects.getObjectSummaries().iterator();
        return new AlibabaStreamIterator(iterator);
    }

}
