package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

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

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalPartition partition, MybatisParamHolder mybatisParamHolder) {
        if (partition == null || partition.getPartitions() == null || partition.getPartitions().isEmpty()) {
            return sqlBuilder;
        }
        for (int i = 0; i < partition.getPartitions().size(); i++) {
            sqlBuilder.append(partition.getPartitions().get(i));
            if (i + 1 < partition.getPartitions().size()) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(") ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
