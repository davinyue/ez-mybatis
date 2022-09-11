package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class MySqlTableConverter extends AbstractConverter<Table> implements Converter<Table> {
    private static volatile MySqlTableConverter instance;

    protected MySqlTableConverter() {
    }

    public static MySqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Table table,
                                        MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" ").append(table.toSqlStruct(type, configuration, mybatisParamHolder)).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
