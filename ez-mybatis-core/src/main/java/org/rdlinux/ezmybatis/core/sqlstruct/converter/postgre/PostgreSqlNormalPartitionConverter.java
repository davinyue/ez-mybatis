package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

/**
 * PostgreSQL 普通分区转换器。
 *
 * <p>PostgreSQL 的分区表通过父表和分区子表表达，表引用不支持 MySQL/Oracle
 * 风格的 {@code PARTITION (...)} 子句，因此非空分区不能直接转换为表引用。
 * 调用方应将目标分区解析为具体物理表名。</p>
 */
public class PostgreSqlNormalPartitionConverter extends AbstractConverter<NormalPartition>
        implements Converter<NormalPartition> {
    /**
     * 转换器单例。
     */
    private static volatile PostgreSqlNormalPartitionConverter instance;

    /**
     * 创建 PostgreSQL 普通分区转换器。
     */
    protected PostgreSqlNormalPartitionConverter() {
    }

    /**
     * 获取 PostgreSQL 普通分区转换器单例。
     *
     * @return PostgreSQL 普通分区转换器
     */
    public static PostgreSqlNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    /**
     * 拒绝生成 PostgreSQL 不支持的表引用分区语法。
     *
     * @param type               SQL 转换类型
     * @param partition          普通分区
     * @param sqlGenerateContext SQL 生成上下文
     */
    @Override
    protected void doBuildSql(Type type, NormalPartition partition, SqlGenerateContext sqlGenerateContext) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return;
        }
        throw new UnsupportedOperationException(
                "PostgreSQL does not support selecting a table partition by name in a table reference; "
                        + "route the partition to the physical tableName instead");
    }
}
