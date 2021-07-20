package com.jstarcraft.cloud.platform.alibaba;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.io.StreamManager;
import com.jstarcraft.core.utility.StringUtility;

public class AlibabaStreamManagerTestCase {

    protected StreamManager getStreamManager() {
        AlibabaStreamManager manager = null;
        return manager;
    }

    @Test
    public void testHaveResource() throws Exception {
        StreamManager manager = getStreamManager();
        String path = "left/middle/right.txt";

        {
            Assert.assertFalse(manager.haveResource(path));
            InputStream stream = manager.retrieveResource(path);
            Assert.assertNull(stream);
        }

        try (InputStream stream = new ByteArrayInputStream(path.getBytes(StringUtility.CHARSET))) {
            manager.saveResource(path, stream);
            Assert.assertTrue(manager.haveResource(path));
        }

        try (InputStream stream = manager.retrieveResource(path)) {
            String content = IOUtils.readLines(stream, StringUtility.CHARSET).get(0);
            Assert.assertEquals(path, content);
        }

        manager.waiveResource(path);
        Assert.assertFalse(manager.haveResource(path));
    }

}
