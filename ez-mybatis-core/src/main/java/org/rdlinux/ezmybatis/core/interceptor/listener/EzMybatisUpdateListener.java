package org.rdlinux.ezmybatis.core.interceptor.listener;

import org.rdlinux.ezmybatis.core.EzUpdate;

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
    default void onBatchUpdate(Collection<?> models) {
    }

    /**
     * 单条替换
     */
    default void onReplace(Object entity) {
    }

    /**
     * 批量替换
     */
    default void onBatchReplace(Collection<?> models) {
    }

    /**
     * 单条条件更新
     */
    default void onEzUpdate(EzUpdate ezUpdate) {
    }

    /**
     * 批量批量更新
     */
    default void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
    }

    default int order() {
        return 0;
    }
}
