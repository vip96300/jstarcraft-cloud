package com.jstarcraft.cloud.platform;

import com.jstarcraft.core.io.StreamManager;

public abstract class CloudStreamManager implements StreamManager {

    protected final String storage;
    
    protected CloudStreamManager(String storage) {
        this.storage = storage;
    }
    
}
