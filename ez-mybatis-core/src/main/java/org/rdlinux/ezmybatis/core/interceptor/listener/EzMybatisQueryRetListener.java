package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.util.List;

/**
 * 查询结构构造结束监听器
 */
public interface EzMybatisQueryRetListener {
    /**
     * 当单条构造结束时
     */
    default <T> T onBuildDone(T model) {
        return model;
    }

    /**
     * 当全部构造结束时
     */
    default void onBatchBuildDone(List<Object> models) {
    }

    /**
     * 拦截器顺序
     */
    default int order() {
        return 0;
    }
}
