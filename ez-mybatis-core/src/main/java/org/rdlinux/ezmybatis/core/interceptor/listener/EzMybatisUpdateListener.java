package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.Collection;

/**
 * 更新事件监听器
 */
public interface EzMybatisUpdateListener {
    /**
     * 单条更新
     */
    default void onUpdate(Object entity) {
    }

    /**
     * 批量更新
     */
    default void onBatchUpdate(Collection<Object> entitys) {
    }

    /**
     * 单条替换
     */
    default void onReplace(Object entity) {
    }

    /**
     * 批量替换
     */
    default void onBatchReplace(Collection<Object> entitys) {
    }

    default int order() {
        return 1;
    }
}
