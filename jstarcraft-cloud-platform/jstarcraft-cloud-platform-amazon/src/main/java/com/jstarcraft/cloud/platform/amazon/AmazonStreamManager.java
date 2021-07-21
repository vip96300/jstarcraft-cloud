package com.jstarcraft.cloud.platform.amazon;

import java.io.InputStream;
import java.util.Iterator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.jstarcraft.cloud.platform.CloudStreamManager;

public class AmazonStreamManager extends CloudStreamManager {

    private AmazonS3 s3;

    public AmazonStreamManager(String storage, AmazonS3 s3) {
        super(storage);
        this.s3 = s3;
    }

    @Override
    public void saveResource(String path, InputStream stream) {
        ObjectMetadata metadata = new ObjectMetadata();
        s3.putObject(storage, path, stream, metadata);
    }

    @Override
    public void waiveResource(String path) {
        s3.deleteObject(storage, path);
    }

    @Override
    public boolean haveResource(String path) {
        return s3.doesObjectExist(storage, path);
    }

    @Override
    public InputStream retrieveResource(String path) {
        try {
            S3Object object = s3.getObject(storage, path);
            return object.getObjectContent().getDelegateStream();
        } catch (AmazonS3Exception exception) {
            return null;
        }
    }

    private class AmazonStreamIterator implements Iterator<String> {

        private Iterator<S3ObjectSummary> iterator;

        private AmazonStreamIterator(Iterator<S3ObjectSummary> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            S3ObjectSummary object = iterator.next();
            return object.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        Iterator<S3ObjectSummary> iterator = s3.listObjects(storage, path).getObjectSummaries().listIterator();
        return new AmazonStreamIterator(iterator);
    }

}
