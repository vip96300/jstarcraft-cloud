package com.jstarcraft.cloud.profile.etcd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.option.JsonOption;
import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.common.option.PropertyOption;
import com.jstarcraft.core.common.option.XmlOption;
import com.jstarcraft.core.common.option.YamlOption;
import com.jstarcraft.core.utility.StringUtility;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;

/**
 * etcd配置管理器
 * 
 * @author Birdy
 *
 */
public class EtcdProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(EtcdProfileManager.class);

    private Client etcd;

    private String format;

    public EtcdProfileManager(Client etcd, String format) {
        this.etcd = etcd;
        this.format = format;
    }

    @Override
    public Option getOption(String name) {
        try {
            List<KeyValue> keyValues = etcd.getKVClient().get(ByteSequence.from(name.getBytes(StringUtility.CHARSET))).get().getKvs();
            String content = keyValues.get(0).getValue().toString(StringUtility.CHARSET);
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
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
