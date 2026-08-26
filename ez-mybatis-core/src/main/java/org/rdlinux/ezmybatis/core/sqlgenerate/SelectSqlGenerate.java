package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

/**
 * 查询SQL生成器接口
 * 定义生成各种查询操作SQL语句的方法
 */
public interface SelectSqlGenerate {
    /**
     * 根据实体ID生成查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param ntClass            实体类型
     * @param id                 实体ID
     * @return 查询SQL语句
     */
    String getSelectByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id);

    /**
     * 根据实体ID集合生成查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param ntClass            实体类型
     * @param ids                实体ID集合
     * @return 查询SQL语句
     */
    String getSelectByIdsSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Collection<?> ids);

    /**
     * 根据EzQuery对象生成查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     * @return 查询SQL语句
     */
    String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query);

    /**
     * 根据EzQuery对象生成计数查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param query              查询对象
     * @return 计数查询SQL语句
     */
    String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query);

    /**
     * 根据数据库表对象生成表存在性查询SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              数据库表对象
     * @return 表存在性查询SQL语句
     */
    String getTableExistsSql(SqlGenerateContext sqlGenerateContext, DbTable table);
}
