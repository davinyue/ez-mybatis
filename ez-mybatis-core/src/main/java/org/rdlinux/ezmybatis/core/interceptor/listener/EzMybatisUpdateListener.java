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
        for (Object model : models) {
            this.onUpdate(model);
        }
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
        for (Object model : models) {
            this.onReplace(model);
        }
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
        for (EzUpdate ezUpdate : ezUpdates) {
            this.onEzUpdate(ezUpdate);
        }
    }

    default int order() {
        return 0;
    }
}
