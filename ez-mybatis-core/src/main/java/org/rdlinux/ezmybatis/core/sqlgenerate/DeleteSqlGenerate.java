package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

/**
 * 删除SQL生成器接口
 * 定生成各种删除操作SQL语句的方法
 */
public interface DeleteSqlGenerate {
    /**
     * 根据实体ID生成删除SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param ntClass            实体类型
     * @param id                 实体ID
     * @return 删除SQL语句
     */
    String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id);

    /**
     * 批量根据实体ID生成删除SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param table              表对象
     * @param ntClass            实体类型
     * @param ids                实体ID集合
     * @return 删除SQL语句
     */
    String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                 Collection<?> ids);

    /**
     * 根据EzDelete对象生成删除SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param delete             删除条件对象
     * @return 删除SQL语句
     */
    String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete);

    /**
     * 根据EzDelete对象集合生成批量删除SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param deletes            删除条件对象集合
     * @return 删除SQL语句
     */
    String getDeleteSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes);
}
