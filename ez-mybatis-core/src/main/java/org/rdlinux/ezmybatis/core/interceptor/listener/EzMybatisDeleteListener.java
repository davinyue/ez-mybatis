package org.rdlinux.ezmybatis.core.interceptor.listener;

import org.rdlinux.ezmybatis.core.EzDelete;

import java.util.Collection;

/**
 * 删除事件监听器
 */
public interface EzMybatisDeleteListener {
    default void onDelete(Object model) {
    }


    default void onBatchDelete(Collection<Object> models) {
        for (Object model : models) {
            this.onDelete(model);
        }
    }


    default void onDeleteById(Object id, Class<?> ntClass) {
    }


    default void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
        for (Object id : ids) {
            this.onDeleteById(id, ntClass);
        }
    }

    default void onEzDelete(EzDelete ezDelete) {
    }


    default void onEzBatchDelete(Collection<EzDelete> ezDeletes) {
        for (EzDelete ezDelete : ezDeletes) {
            this.onEzDelete(ezDelete);
        }
    }

    default int order() {
        return 0;
    }
}
