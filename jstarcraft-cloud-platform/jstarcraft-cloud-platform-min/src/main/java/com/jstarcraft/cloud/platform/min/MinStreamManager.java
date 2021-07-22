package com.jstarcraft.cloud.platform.min;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import com.jstarcraft.cloud.platform.CloudStreamManager;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
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
    public void saveResource(String s, InputStream inputStream) {
        PutObjectArgs args = null;
        try {
            args = PutObjectArgs.builder().stream(inputStream, inputStream.available(), -1).bucket(storage).object(s).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            io.putObject(args);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void waiveResource(String s) {
        RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(storage).object(s).build();
        try {
            io.removeObject(args);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean haveResource(String s) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(storage).object(s).build();
        try {
            GetObjectResponse response = io.getObject(args);
            return response != null;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public InputStream retrieveResource(String s) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(storage).object(s).build();
        try {
            GetObjectResponse response = io.getObject(args);
            return response;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class MinioStreamIterator implements Iterator<String> {

        private Iterator<Result<Item>> iterator;

        private MinioStreamIterator(Iterator<Result<Item>> iterator) {
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
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<String> iterateResources(String s) {
        ListObjectsArgs args = ListObjectsArgs.builder().bucket(storage).prefix(s).recursive(true).build();
        Iterator<Result<Item>> results = io.listObjects(args).iterator();
        return new MinioStreamIterator(results);
    }
}
