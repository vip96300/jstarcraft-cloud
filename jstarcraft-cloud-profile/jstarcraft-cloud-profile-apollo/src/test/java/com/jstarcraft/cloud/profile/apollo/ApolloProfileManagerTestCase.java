package com.jstarcraft.cloud.profile.apollo;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.jstarcraft.core.common.option.Option;

public class ApolloProfileManagerTestCase {

    @Test
    public void test() {
        Config apollo = ConfigService.getConfig("application");
        Assert.assertEquals("random", apollo.getProperty("race", null));

        ApolloProfileManager manager = new ApolloProfileManager(ConfigFileFormat.Properties);
        Option configurator = manager.getOption("application");
        Assert.assertEquals("random", configurator.getString("race"));
    }

}
