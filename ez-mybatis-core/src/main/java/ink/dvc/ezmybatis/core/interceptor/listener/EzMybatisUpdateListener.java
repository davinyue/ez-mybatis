package ink.dvc.ezmybatis.core.interceptor.listener;

import java.util.List;

/**
 * 更新事件监听器
 */
public interface EzMybatisUpdateListener {
    void onUpdate(Object entity);

    void onBatchUpdate(List<Object> entity);
}
