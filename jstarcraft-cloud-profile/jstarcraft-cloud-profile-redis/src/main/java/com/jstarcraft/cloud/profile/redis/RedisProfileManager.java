package com.jstarcraft.cloud.profile.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;

/**
 * Redis配置管理器
 * 
 * @author Birdy
 *
 */
public class RedisProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisProfileManager.class);

    private Redisson redis;

    private String format;

    public RedisProfileManager(Redisson redis, String format) {
        this.redis = redis;
        this.format = format;
    }

    @Override
    public Option getOption(String name) {
        RBucket<String> bucket = redis.getBucket(name);
        String content = bucket.get();
        switch (format) {
        case "json":
            return new JsonOption(content);
        case "properties":
            return new PropertyOption(content);
        case "xml":
            return new XmlOption(content);
        case "yaml":
            return new YamlOption(content);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void registerMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
