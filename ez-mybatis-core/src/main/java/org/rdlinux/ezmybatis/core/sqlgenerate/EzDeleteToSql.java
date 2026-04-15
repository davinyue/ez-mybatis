package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzDelete;

import java.util.Collection;

/**
 * EzDelete转SQL接口
 * 定义将EzDelete对象转换为SQL语句的方法
 */
public interface EzDeleteToSql {
    /**
     * 将单个EzDelete对象转换为SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param delete             EzDelete删除对象
     * @return 生成的SQL语句
     */
    String toSql(SqlGenerateContext sqlGenerateContext, EzDelete delete);

    /**
     * 将EzDelete对象集合转换为SQL语句
     *
     * @param sqlGenerateContext SQL生成上下文
     * @param deletes            EzDelete删除对象集合
     * @return 生成的SQL语句
     */
    String toSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes);
}
