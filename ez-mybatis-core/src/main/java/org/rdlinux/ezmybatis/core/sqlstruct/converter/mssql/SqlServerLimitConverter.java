package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class SqlServerLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile SqlServerLimitConverter instance;

    protected SqlServerLimitConverter() {
    }

    public static SqlServerLimitConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerLimitConverter.class) {
                if (instance == null) {
                    instance = new SqlServerLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Limit limit, SqlGenerateContext sqlGenerateContext) {
        if (limit == null) {
            return;
        }
        //OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY
        sqlGenerateContext.getSqlBuilder().append("OFFSET 0 ROWS FETCH NEXT ").append(limit.getSize())
                .append(" ROWS ONLY ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }
}
