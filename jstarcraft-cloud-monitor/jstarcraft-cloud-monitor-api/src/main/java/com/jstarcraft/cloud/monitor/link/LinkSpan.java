package com.jstarcraft.cloud.monitor.link;

import java.time.Instant;

import com.jstarcraft.core.common.lifecycle.LifecycleState;

/**
 * 追踪单元
 * 
 * @author Birdy
 *
 */
public interface LinkSpan {

    /**
     * 获取根标识
     * 
     * @return
     */
    String getRoot();

    /**
     * 获取双亲标识
     * 
     * @return
     */
    String getParent();

    /**
     * 获取单元标识
     * 
     * @return
     */
    String getId();

    /**
     * 获取单元名称
     * 
     * @return
     */
    String getName();

    /**
     * 获取单元开始
     * 
     * @return
     */
    Instant getBegin();

    /**
     * 获取单元结束
     * 
     * @return
     */
    Instant getEnd();

    /**
     * 获取单元状态
     * 
     * @return
     */
    LifecycleState getState();

    /**
     * 获取单元上下文
     * 
     * @return
     */
    LinkContext getContext();

    /**
     * 获取单元属性
     * 
     * @param key
     * @return
     */
    String getProperty(String key);

    /**
     * 设置单元属性
     * 
     * @param key
     * @param value
     */
    void setProperty(String key, String value);

}
