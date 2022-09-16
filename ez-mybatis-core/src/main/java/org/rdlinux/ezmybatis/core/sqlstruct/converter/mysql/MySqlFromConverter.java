package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class MySqlFromConverter extends AbstractConverter<From> implements Converter<From> {
    private static volatile MySqlFromConverter instance;

    protected MySqlFromConverter() {
    }

    public static MySqlFromConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFromConverter.class) {
                if (instance == null) {
                    instance = new MySqlFromConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, From from,
                                        MybatisParamHolder mybatisParamHolder) {
        Table fromTable = from.getTable();
        if (type == Type.SELECT || type == Type.DELETE) {
            sqlBuilder.append(" FROM ");
        }
        Converter<Table> converter = EzMybatisContent.getConverter(configuration, Table.class);
        return converter.toSqlPart(type, sqlBuilder, configuration, fromTable, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
