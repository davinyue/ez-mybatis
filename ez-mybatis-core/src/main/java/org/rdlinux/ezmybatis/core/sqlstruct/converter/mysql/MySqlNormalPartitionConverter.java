package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlNormalPartitionConverter extends AbstractConverter<NormalPartition> implements Converter<NormalPartition> {
    private static volatile MySqlNormalPartitionConverter instance;

    protected MySqlNormalPartitionConverter() {
    }

    public static MySqlNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    /**
     * 将普通分区转换为 MySQL 表分区片段。
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
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" PARTITION (");
        for (int i = 0; i < partition.getPartitions().size(); i++) {
            sqlBuilder.append(SqlEscaping.nameEscaping(partition.getPartitions().get(i)));
            if (i + 1 < partition.getPartitions().size()) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(") ");
    }

}
