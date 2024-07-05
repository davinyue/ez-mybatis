package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile PostgreSqlPageConverter instance;

    protected PostgreSqlPageConverter() {
    }

    public static PostgreSqlPageConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlPageConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlPageConverter();
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
        return sqlBuilder.append(" LIMIT ").append(limit.getSize()).append(" OFFSET ").append(limit.getSkip())
                .append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
