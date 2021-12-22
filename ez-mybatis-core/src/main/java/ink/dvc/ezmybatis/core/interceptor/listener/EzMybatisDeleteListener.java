package ink.dvc.ezmybatis.core.interceptor.listener;

import java.util.List;

/**
 * 删除事件监听器
 */
public interface EzMybatisDeleteListener {
    void onDelete(Object entity);

    void onBatchDelete(List<Object> entitys);

    void onDeleteById(Object id, Class<?> ntClass);

    void onBatchDeleteById(List<Object> ids, Class<?> ntClass);

    default int order() {
        return 1;
    }
}
