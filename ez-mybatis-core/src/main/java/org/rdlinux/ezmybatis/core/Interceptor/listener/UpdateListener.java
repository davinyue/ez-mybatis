package org.rdlinux.ezmybatis.core.Interceptor.listener;

import java.util.List;

/**
 * 更新事件监听器
 */
public interface UpdateListener {
    void onUpdate(Object entity);

    void onBatchUpdate(List<Object> entity);
}
