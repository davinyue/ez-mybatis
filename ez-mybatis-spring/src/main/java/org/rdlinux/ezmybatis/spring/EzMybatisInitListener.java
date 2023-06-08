package org.rdlinux.ezmybatis.spring;

import org.rdlinux.ezmybatis.EzMybatisConfig;

/**
 * ez mybatis 初始监听器
 */
public interface EzMybatisInitListener {
    /**
     * 当处理完毕后事件
     */
    void onDone(EzMybatisConfig ezMybatisConfig);

    /**
     * 执行顺序, 越小越优化
     */
    default int order() {
        return 1;
    }
}
