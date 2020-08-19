package com.jstarcraft.cloud.profile.apollo;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

public class ApolloProfileManagerTestCase {

    @Test
    public void test() {
        Config apollo = ConfigService.getConfig("application");
        Assert.assertEquals("random", apollo.getProperty("race", null));
    }

}
