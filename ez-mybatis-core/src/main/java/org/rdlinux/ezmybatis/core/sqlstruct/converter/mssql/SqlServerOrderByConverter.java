package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlOrderByConverter;

public class SqlServerOrderByConverter extends MySqlOrderByConverter implements Converter<OrderBy> {
    private static volatile SqlServerOrderByConverter instance;

    protected SqlServerOrderByConverter() {
    }

    public static SqlServerOrderByConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerOrderByConverter.class) {
                if (instance == null) {
                    instance = new SqlServerOrderByConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       OrderBy orderBy, MybatisParamHolder mybatisParamHolder) {
        EzQuery<?> query = orderBy.getQuery();
        if (query.getPage() == null && query.getLimit() == null) {
            return sqlBuilder;
        }
        if (orderBy.getItems() == null || orderBy.getItems().isEmpty()) {
            return sqlBuilder.append(" ORDER BY 1 ");
        } else {
            return super.doBuildSql(type, sqlBuilder, configuration, orderBy, mybatisParamHolder);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }
}
