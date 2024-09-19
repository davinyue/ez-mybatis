package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;

public class SqlServerSqlTableConverter extends AbstractConverter<SqlTable> implements Converter<SqlTable> {
    private static volatile SqlServerSqlTableConverter instance;

    protected SqlServerSqlTableConverter() {
    }

    public static SqlServerSqlTableConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerSqlTableConverter.class) {
                if (instance == null) {
                    instance = new SqlServerSqlTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SqlTable table, MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" (").append(table.getSql()).append(") ");
        if (type == Type.SELECT) {
            sqlBuilder.append(table.getAlias()).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }
}
