package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

public class MySqlSubPartitionConverter extends AbstractConverter<SubPartition> implements Converter<SubPartition> {
    private static volatile MySqlSubPartitionConverter instance;

    protected MySqlSubPartitionConverter() {
    }

    public static MySqlSubPartitionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSubPartitionConverter.class) {
                if (instance == null) {
                    instance = new MySqlSubPartitionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        SubPartition partition, MybatisParamHolder mybatisParamHolder) {
        return MySqlNormalPartitionConverter.getInstance().doToSqlPart(type, sqlBuilder, configuration, partition,
                mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
