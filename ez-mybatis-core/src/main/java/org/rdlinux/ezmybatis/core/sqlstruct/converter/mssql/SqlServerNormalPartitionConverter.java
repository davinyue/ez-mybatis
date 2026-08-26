package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

/**
 * SQL Server 普通分区转换器。
 *
 * <p>SQL Server 的分区由 partition function 和 partition scheme 管理，表引用不支持
 * MySQL/Oracle 风格的 {@code PARTITION (...)} 子句。目标分区应通过分区键条件或数据库
 * 原生分区函数表达，不能直接作为表引用后缀渲染。</p>
 */
public class SqlServerNormalPartitionConverter extends AbstractConverter<NormalPartition>
        implements Converter<NormalPartition> {
    /**
     * 转换器单例。
     */
    private static volatile SqlServerNormalPartitionConverter instance;

    /**
     * 创建 SQL Server 普通分区转换器。
     */
    protected SqlServerNormalPartitionConverter() {
    }

    /**
     * 获取 SQL Server 普通分区转换器单例。
     *
     * @return SQL Server 普通分区转换器
     */
    public static SqlServerNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new SqlServerNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    /**
     * 拒绝生成 SQL Server 不支持的表引用分区语法。
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
                "SQL Server does not support selecting a table partition by name in a table reference; "
                        + "use a partition key condition or route to a physical tableName instead");
    }
}
