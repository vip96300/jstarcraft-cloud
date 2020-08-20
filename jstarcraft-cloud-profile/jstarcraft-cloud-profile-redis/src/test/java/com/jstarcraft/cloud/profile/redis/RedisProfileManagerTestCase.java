package com.jstarcraft.cloud.profile.redis;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import com.jstarcraft.core.common.configuration.Configurator;

public class RedisProfileManagerTestCase {

    private static Redisson redis;

    private static RKeys keys;

    @BeforeClass
    public static void start() throws Exception {
        // 注意此处的编解码器
        Codec codec = new StringCodec();
        Config configuration = new Config();
        configuration.setCodec(codec);
        configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");

        redis = (Redisson) Redisson.create(configuration);
        keys = redis.getKeys();
        keys.flushdb();
    }

    @AfterClass
    public static void stop() throws Exception {
        keys.flushdb();
        redis.shutdown();
    }

    @Test
    public void test() {
        String name = "jstarcraft";
        RBucket<String> bucket = redis.getBucket(name);
        bucket.set("race=random");
        RedisProfileManager manager = new RedisProfileManager(redis, "properties");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        bucket.delete();
    }

}
