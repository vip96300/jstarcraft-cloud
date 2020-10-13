package com.jstarcraft.cloud.profile.etcd;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.common.option.Option;
import com.jstarcraft.core.utility.StringUtility;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;

public class EtcdProfileManagerTestCase {

    @Test
    public void test() throws Exception {
        Client etcd = Client.builder().endpoints("http://127.0.0.1:2379").build();
        KV keyValue = etcd.getKVClient();
        ByteSequence key = ByteSequence.from("jstarcraft".getBytes(StringUtility.CHARSET));
        ByteSequence value = ByteSequence.from("race=random".getBytes(StringUtility.CHARSET));
        keyValue.put(key, value).get();
        EtcdProfileManager manager = new EtcdProfileManager(etcd, "properties");
        Option configurator = manager.getOption("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        keyValue.delete(key);
        etcd.close();
    }

}
