package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.Collection;

/**
 * 插入事件监听器
 */
public interface EzMybatisInsertListener {
    /**
     * 当执行插入时
     */
    default void onInsert(Object model) {
    }

    /**
     * 当执行批量插入时
     */
    default void onBatchInsert(Collection<?> models) {
        for (Object model : models) {
            this.onInsert(model);
        }
    }

    /**
     * 拦截器顺序
     */
    default int order() {
        return 0;
    }
}
