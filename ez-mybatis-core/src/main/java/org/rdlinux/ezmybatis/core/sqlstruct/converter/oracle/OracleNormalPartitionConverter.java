package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

public class OracleNormalPartitionConverter extends AbstractConverter<NormalPartition> implements Converter<NormalPartition> {
    private static volatile OracleNormalPartitionConverter instance;

    protected OracleNormalPartitionConverter() {
    }

    public static OracleNormalPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleNormalPartitionConverter.class) {
                if (instance == null) {
                    instance = new OracleNormalPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalPartition partition, MybatisParamHolder mybatisParamHolder) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" PARTITION(").append(partition.getPartitions().get(0)).append(") ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
