package com.jstarcraft.cloud.configuration.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTestCase {

    @Autowired
    private ConfigProperties properties;

    /**
     * 测试SpringBoot上下文
     */
    @Test
    public void testContext() {
        Assert.assertEquals("mock-config", properties.getConfig());
    }

}
