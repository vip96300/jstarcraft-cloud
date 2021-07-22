package com.jstarcraft.cloud.platform.tencent;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;

import java.io.InputStream;
import java.util.Iterator;

/**
 * @author Huang Hong Fei
 * @createAt 2021/7/22
 * @description
 */
public class TencentStreamManager extends CloudStreamManager {

    private COSClient client;

    protected TencentStreamManager(String storage) {
        super(storage);
    }

    public TencentStreamManager(String storage, COSClient client){
        this(storage);
        this.client=client;
    }

    @Override
    public void saveResource(String s, InputStream inputStream) {
        PutObjectRequest request=new PutObjectRequest(storage,s,inputStream,new ObjectMetadata());
        client.putObject(request);
    }

    @Override
    public void waiveResource(String s) {
        DeleteObjectRequest request=new DeleteObjectRequest(storage,s);
        client.deleteObject(request);
    }

    @Override
    public boolean haveResource(String s) {
        return client.doesObjectExist(storage,s);
    }

    @Override
    public InputStream retrieveResource(String s) {
        GetObjectRequest request=new GetObjectRequest(storage,s);
        COSObject object=null;
        try {
            object=client.getObject(request);
        }catch (CosServiceException e){
            return null;
        }
        return object.getObjectContent();
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
    public Iterator<String> iterateResources(String s) {
        ObjectListing list=client.listObjects(storage,s);
        return new TencentStreamIterator(list.getObjectSummaries().iterator());
    }
}
