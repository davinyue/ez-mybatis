package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, SubPartition partition, SqlGenerateContext sqlGenerateContext) {
        MySqlNormalPartitionConverter.getInstance().doBuildSql(type, partition, sqlGenerateContext);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
