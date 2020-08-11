package com.jstarcraft.cloud.governance;

import java.util.Map;
import java.util.Objects;

/**
 * 默认治理实例
 * 
 * @author Birdy
 *
 */
public class DefaultGovernanceInstance implements GovernanceInstance {

    /** 实例标识 */
    private String id;

    /** 实例名称 */
    private String name;

    /** 实例域名 */
    private String host;

    /** 实例端口 */
    private int port;

    /** 实例元信息 */
    private Map<String, String> metadata;

    public DefaultGovernanceInstance(String id, String name, String host, int port, Map<String, String> metadata) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.metadata = metadata;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, host, port);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        DefaultGovernanceInstance that = (DefaultGovernanceInstance) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(host, that.host) && port == that.port;
    }

    @Override
    public String toString() {
        return "DefaultGovernanceInstance [id=" + id + ", name=" + name + ", host=" + host + ", port=" + port + ", metadata=" + metadata + "]";
    }

}
