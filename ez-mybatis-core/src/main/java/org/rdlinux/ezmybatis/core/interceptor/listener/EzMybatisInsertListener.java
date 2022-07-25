package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.Collection;

/**
 * 插入事件监听器
 */
public interface EzMybatisInsertListener {
    void onInsert(Object entity);

    void onBatchInsert(Collection<Object> entitys);

    default int order() {
        return 1;
    }
}
