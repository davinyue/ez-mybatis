package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;

public class MySqlSqlTableConverter extends AbstractConverter<SqlTable> implements Converter<SqlTable> {
    private static volatile MySqlSqlTableConverter instance;

    protected MySqlSqlTableConverter() {
    }

    public static MySqlSqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SqlTable table, MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" (").append(table.getSql()).append(") ");
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE) {
            sqlBuilder.append(table.getAlias()).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
