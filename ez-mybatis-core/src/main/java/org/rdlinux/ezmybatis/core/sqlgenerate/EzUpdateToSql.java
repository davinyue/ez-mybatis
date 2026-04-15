package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzUpdate;

import java.util.Collection;

/**
 * EzUpdate转SQL接口
 *定义将EzUpdate对象转换为SQL语句的方法
 */
public interface EzUpdateToSql {
    /**
     * 将单个EzUpdate对象转换为SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param update             EzUpdate更新对象
     * @return 生成的SQL语句
     */
    String toSql(SqlGenerateContext sqlGenerateContext, EzUpdate update);

    /**
     * 将EzUpdate对象集合转换为SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param updates            EzUpdate更新对象集合
     * @return 生成的SQL语句
     */
    String toSql(SqlGenerateContext sqlGenerateContext, Collection<EzUpdate> updates);
}
