package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class SqlServerPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile SqlServerPageConverter instance;

    protected SqlServerPageConverter() {
    }

    public static SqlServerPageConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerPageConverter.class) {
                if (instance == null) {
                    instance = new SqlServerPageConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Page limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        return sqlBuilder.append(" OFFSET ").append(limit.getSkip()).append(" ROWS FETCH NEXT ")
                .append(limit.getSize()).append(" ROWS ONLY ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }
}
