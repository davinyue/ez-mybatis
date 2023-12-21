package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile PostgreSqlLimitConverter instance;

    protected PostgreSqlLimitConverter() {
    }

    public static PostgreSqlLimitConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlLimitConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Limit limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        return sqlBuilder.append(" LIMIT ").append(limit.getSize()).append(" OFFSET ").append(limit.getSkip())
                .append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
