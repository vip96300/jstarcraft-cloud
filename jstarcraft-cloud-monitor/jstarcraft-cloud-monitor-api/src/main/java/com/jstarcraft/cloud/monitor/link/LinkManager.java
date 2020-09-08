package com.jstarcraft.cloud.monitor.link;

/**
 * 追踪管理器
 * 
 * @author Birdy
 *
 */
public interface LinkManager {

    /**
     * 开启追踪单元
     * 
     * @param context
     * @return
     */
    LinkSpan openSpan(LinkContext context);

    /**
     * 获取追踪单元
     * 
     * @return
     */
    LinkSpan getSpan();

    /**
     * 关闭追踪单元
     * 
     * @param span
     */
    void closeSpan(LinkSpan span);

}
