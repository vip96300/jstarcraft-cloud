package com.jstarcraft.cloud.registration;

import java.util.List;
import java.util.Map;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

/**
 * 抽象服务管理器
 * 
 * @author Birdy
 *
 * @param <T>
 */
public abstract class AbstractServerManager<T extends Server> implements ServerManager<T> {

    /** 服务标识 */
    protected String serviceId;

    /** 服务集合 */
    protected ServerList<T> serverList;

    protected AbstractServerManager(IClientConfig clientConfig, ServerList<T> serverList) {
        this.serviceId = clientConfig.getClientName();
        this.serverList = serverList;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    /**
     * 获取元数据
     * 
     * @param server
     * @return
     */
    protected abstract Map<String, String> getMatadata(T server);

    @Override
    public List<T> getInitialListOfServers() {
        List<T> servers = serverList.getInitialListOfServers();
        for (T server : servers) {
            Map<String, String> metadata = getMatadata(server);
        }
        return servers;
    }

    @Override
    public List<T> getUpdatedListOfServers() {
        List<T> servers = serverList.getUpdatedListOfServers();
        for (T server : servers) {
            Map<String, String> metadata = getMatadata(server);
        }
        return servers;
    }

}
