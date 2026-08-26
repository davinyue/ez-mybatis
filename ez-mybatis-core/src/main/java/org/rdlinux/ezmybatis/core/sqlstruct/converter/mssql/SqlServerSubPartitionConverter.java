package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

/**
 * SQL Server 子分区转换器。
 *
 * <p>SQL Server 不支持在表引用中使用 MySQL/Oracle 风格的子分区后缀，分区定位应通过
 * 分区键条件或数据库原生分区函数表达。</p>
 */
public class SqlServerSubPartitionConverter extends AbstractConverter<SubPartition>
        implements Converter<SubPartition> {
    /**
     * 转换器单例。
     */
    private static volatile SqlServerSubPartitionConverter instance;

    /**
     * 创建 SQL Server 子分区转换器。
     */
    protected SqlServerSubPartitionConverter() {
    }

    /**
     * 获取 SQL Server 子分区转换器单例。
     *
     * @return SQL Server 子分区转换器
     */
    public static SqlServerSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new SqlServerSubPartitionConverter();
                }
            }
        }
        return instance;
    }

    /**
     * 拒绝生成 SQL Server 不支持的表引用子分区语法。
     *
     * @param type               SQL 转换类型
     * @param partition          子分区
     * @param sqlGenerateContext SQL 生成上下文
     */
    @Override
    protected void doBuildSql(Type type, SubPartition partition, SqlGenerateContext sqlGenerateContext) {
        SqlServerNormalPartitionConverter.getInstance().doBuildSql(type, partition, sqlGenerateContext);
    }
}
