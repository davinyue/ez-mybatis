package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzQuery;

/**
 * EzQuery转SQL接口
 * 定义将EzQuery对象转换为SQL语句的方法
 */
public interface EzQueryToSql {
    /**
     * 将EzQuery对象转换为查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              EzQuery查询对象
     * @return 生成的查询SQL语句
     */
    String toSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query);

    /**
     * 将EzQuery对象转换为计数SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              EzQuery查询对象
     * @return 生成的计数SQL语句
     */
    String toCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query);
}
