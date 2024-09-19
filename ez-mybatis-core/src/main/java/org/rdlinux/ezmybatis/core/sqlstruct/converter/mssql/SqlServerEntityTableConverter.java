package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SqlServerEntityTableConverter extends AbstractConverter<EntityTable> implements Converter<EntityTable> {
    private static volatile SqlServerEntityTableConverter instance;

    protected SqlServerEntityTableConverter() {
    }

    public static SqlServerEntityTableConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerEntityTableConverter.class) {
                if (instance == null) {
                    instance = new SqlServerEntityTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       EntityTable table, MybatisParamHolder mybatisParamHolder) {
        return SqlServerDbTableConverter.getInstance().doBuildSql(type, sqlBuilder, configuration, table,
                mybatisParamHolder);
    }
}
