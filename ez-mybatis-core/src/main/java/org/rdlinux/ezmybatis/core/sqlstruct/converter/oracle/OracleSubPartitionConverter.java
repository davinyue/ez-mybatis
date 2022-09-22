package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

public class OracleSubPartitionConverter extends AbstractConverter<SubPartition> implements Converter<SubPartition> {
    private static volatile OracleSubPartitionConverter instance;

    protected OracleSubPartitionConverter() {
    }

    public static OracleSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new OracleSubPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        SubPartition partition, MybatisParamHolder mybatisParamHolder) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return sqlBuilder;
        }
        return sqlBuilder.append(" SUBPARTITION(").append(partition.getPartitions().get(0)).append(") ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
