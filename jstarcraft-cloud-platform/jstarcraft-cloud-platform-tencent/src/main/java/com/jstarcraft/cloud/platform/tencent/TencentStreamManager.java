package com.jstarcraft.cloud.platform.tencent;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectMetadata;

/**
 * Huang Hong Fei
 * 
 * @author Birdy
 *
 */
public class TencentStreamManager extends CloudStreamManager {

    private COSClient cos;

    public TencentStreamManager(String storage, COSClient cos) {
        super(storage);
        this.cos = cos;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        ObjectMetadata metadata = new ObjectMetadata();
        cos.putObject(storage, path, stream, metadata);
    }

    @Override
    public void waiveResource(String path) {
        cos.deleteObject(storage, path);
    }

    @Override
    public boolean haveResource(String path) {
        return cos.doesObjectExist(storage, path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        try {
            COSObject object = cos.getObject(storage, path);
            return object.getObjectContent();
        } catch (CosServiceException exception) {
            return null;
        }
    }

    private class TencentStreamIterator implements Iterator<String> {

        private Iterator<COSObjectSummary> iterator;

        private TencentStreamIterator(Iterator<COSObjectSummary> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            COSObjectSummary object = iterator.next();
            return object.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        Iterator<COSObjectSummary> iterator = cos.listObjects(storage, path).getObjectSummaries().iterator();
        return new TencentStreamIterator(iterator);
    }
}
