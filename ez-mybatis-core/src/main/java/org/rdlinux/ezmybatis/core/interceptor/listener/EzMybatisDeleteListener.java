package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.Collection;

/**
 * 删除事件监听器
 */
public interface EzMybatisDeleteListener {
    void onDelete(Object entity);

    void onBatchDelete(Collection<Object> entitys);

    void onDeleteById(Object id, Class<?> ntClass);

    void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass);

    default int order() {
        return 1;
    }
}
