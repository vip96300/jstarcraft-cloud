package com.jstarcraft.cloud.platform.min;

import com.jstarcraft.cloud.platform.CloudStreamManager;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * @author Huang Hong Fei
 * @createAt 2021/7/22
 * @description
 */
public class MinioStreamManager extends CloudStreamManager {

    private MinioClient client;

    protected MinioStreamManager(String storage) {
        super(storage);
    }

    public MinioStreamManager(String storage, MinioClient client){
        this(storage);
        this.client=client;
    }

    @Override
    public void saveResource(String s, InputStream inputStream) {
        PutObjectArgs args= null;
        try {
            args = PutObjectArgs.builder()
                    .stream(inputStream,inputStream.available(), -1)
                    .bucket(storage)
                    .object(s)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.putObject(args);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void waiveResource(String s) {
        RemoveObjectArgs args= RemoveObjectArgs.builder()
                .bucket(storage)
                .object(s)
                .build();
        try {
            client.removeObject(args);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean haveResource(String s) {
        GetObjectArgs args= GetObjectArgs.builder()
                .bucket(storage)
                .object(s)
                .build();
        try {
            GetObjectResponse response=client.getObject(args);
            return response!=null;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public InputStream retrieveResource(String s) {
        GetObjectArgs args= GetObjectArgs.builder()
                .bucket(storage)
                .object(s)
                .build();
        try {
            GetObjectResponse response=client.getObject(args);
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
        ListObjectsArgs args=ListObjectsArgs.builder()
                .bucket(storage)
                .build();
        Iterator<Result<Item>> results=client.listObjects(args).iterator();
        return new MinioStreamIterator(results);
    }
}
