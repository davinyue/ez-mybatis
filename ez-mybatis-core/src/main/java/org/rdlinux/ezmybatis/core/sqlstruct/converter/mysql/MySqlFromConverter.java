package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, From from, SqlGenerateContext sqlGenerateContext) {
        Table fromTable = from.getTable();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (type == Type.SELECT || type == Type.DELETE) {
            sqlBuilder.append(" FROM ");
        }
        Converter<?> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                fromTable.getClass());
        converter.buildSql(type, fromTable, sqlGenerateContext);
    }

}
