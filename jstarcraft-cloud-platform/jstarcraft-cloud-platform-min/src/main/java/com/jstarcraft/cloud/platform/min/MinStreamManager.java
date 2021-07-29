package com.jstarcraft.cloud.platform.min;

import java.io.InputStream;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import com.jstarcraft.core.io.exception.StreamException;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;

/**
 * 
 * @author Huang Hong Fei
 *
 */
public class MinStreamManager extends CloudStreamManager {

    private MinioClient io;

    public MinStreamManager(String storage, MinioClient io) {
        super(storage);
        this.io = io;
    }

    @Override
    public void saveResource(String path, InputStream inputStream) {
        PutObjectArgs request = PutObjectArgs.builder().bucket(storage).object(path).stream(inputStream, -1, PutObjectArgs.MIN_MULTIPART_SIZE).build();
        try {
            io.putObject(request);
        } catch (Exception exception) {
            throw new StreamException(exception);
        }
    }

    @Override
    public void waiveResource(String path) {
        RemoveObjectArgs request = RemoveObjectArgs.builder().bucket(storage).object(path).build();
        try {
            io.removeObject(request);
        } catch (Exception exception) {
            throw new StreamException(exception);
        }
    }

    @Override
    public boolean haveResource(String path) {
        StatObjectArgs request = StatObjectArgs.builder().bucket(storage).object(path).build();
        try {
            StatObjectResponse response = io.statObject(request);
            return response != null;
        } catch (ErrorResponseException exception) {
            String code = exception.errorResponse().code();
            if (code.equals("NoSuchKey")) {
                return false;
            }
            throw new StreamException(exception);
        } catch (Exception exception) {
            throw new StreamException(exception);
        }
    }

    @Override
    public InputStream retrieveResource(String path) {
        GetObjectArgs request = GetObjectArgs.builder().bucket(storage).object(path).build();
        try {
            GetObjectResponse response = io.getObject(request);
            return response;
        } catch (ErrorResponseException exception) {
            String code = exception.errorResponse().code();
            if (code.equals("NoSuchKey")) {
                return null;
            }
            throw new StreamException(exception);
        } catch (Exception exception) {
            throw new StreamException(exception);
        }
    }

    private class MinStreamIterator implements Iterator<String> {

        private Iterator<Result<Item>> iterator;

        private MinStreamIterator(Iterator<Result<Item>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            Result<Item> object = iterator.next();
            try {
                return object.get().objectName();
            } catch (Exception exception) {
                throw new StreamException(exception);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String path) {
        ListObjectsArgs request = ListObjectsArgs.builder().bucket(storage).prefix(path).recursive(true).build();
        Iterable<Result<Item>> response = io.listObjects(request);
        Iterator<Result<Item>> iterator = response.iterator();
        return new MinStreamIterator(iterator);
    }
}
