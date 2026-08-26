package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

/**
 * PostgreSQL 子分区转换器。
 *
 * <p>PostgreSQL 不支持在表引用中使用 MySQL/Oracle 风格的子分区后缀，具体分区应由
 * 动态物理表名路由决定。</p>
 */
public class PostgreSqlSubPartitionConverter extends AbstractConverter<SubPartition>
        implements Converter<SubPartition> {
    /**
     * 转换器单例。
     */
    private static volatile PostgreSqlSubPartitionConverter instance;

    /**
     * 创建 PostgreSQL 子分区转换器。
     */
    protected PostgreSqlSubPartitionConverter() {
    }

    /**
     * 获取 PostgreSQL 子分区转换器单例。
     *
     * @return PostgreSQL 子分区转换器
     */
    public static PostgreSqlSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlSubPartitionConverter();
                }
            }
        }
        return instance;
    }

    /**
     * 拒绝生成 PostgreSQL 不支持的表引用子分区语法。
     *
     * @param type               SQL 转换类型
     * @param partition          子分区
     * @param sqlGenerateContext SQL 生成上下文
     */
    @Override
    protected void doBuildSql(Type type, SubPartition partition, SqlGenerateContext sqlGenerateContext) {
        PostgreSqlNormalPartitionConverter.getInstance().doBuildSql(type, partition, sqlGenerateContext);
    }
}
