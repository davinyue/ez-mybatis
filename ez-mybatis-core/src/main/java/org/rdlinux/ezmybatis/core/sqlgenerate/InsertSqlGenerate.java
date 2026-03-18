package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

/**
 * 插入SQL生成器接口
 * 定义生成各种插入操作SQL语句的方法
 */
public interface InsertSqlGenerate {
    /**
     * 获取单个实体插入SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param entity             实体对象
     * @return 插入SQL语句
     */
    String getInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Object entity);

    /**
     * 获取批量插入SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param models             实体对象集合
     * @return 批量插入SQL语句
     */
    String getBatchInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<Object> models);

    /**
     * 获取JDBC批量插入SQL
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param models             实体对象集合
     * @return JDBC批量插入SQL对象
     */
    EzJdbcBatchSql getJdbcBatchInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models);

    /**
     * 获取基于查询的插入SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param query              查询对象
     * @return 基于查询的插入SQL语句
     */
    String getInsertByQuerySql(SqlGenerateContext sqlGenerateContext, Table table, EzQuery<?> query);
}
