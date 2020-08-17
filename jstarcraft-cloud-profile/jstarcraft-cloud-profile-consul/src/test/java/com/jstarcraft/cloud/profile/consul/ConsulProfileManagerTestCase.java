package com.jstarcraft.cloud.profile.consul;

import org.junit.Assert;
import org.junit.Test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.QueryParams;
import com.jstarcraft.core.common.configuration.Configurator;

public class ConsulProfileManagerTestCase {

    @Test
    public void test() {
        ConsulRawClient client = new ConsulRawClient("localhost", 8500);
        ConsulClient consul = new ConsulClient(client);
        consul.setKVValue("jstarcraft", "race=random", QueryParams.DEFAULT);
        ConsulProfileManager manager = new ConsulProfileManager(consul, "properties");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        consul.deleteKVValue("jstarcraft", QueryParams.DEFAULT);
    }

}
