package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.List;

/**
 * 删除事件监听器
 */
public interface DeleteListener {
    void onDelete(Object entity);

    void onBatchDelete(List<Object> entity);

    void onDeleteByPrimaryKey(Object id, Class<?> ntClass);

    void onDeleteByPrimaryKeys(List<Object> ids, Class<?> ntClass);
}
