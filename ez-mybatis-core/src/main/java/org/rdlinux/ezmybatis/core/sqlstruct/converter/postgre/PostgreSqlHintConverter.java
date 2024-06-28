package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlHintConverter extends AbstractConverter<SqlHint> implements Converter<SqlHint> {
    private static volatile PostgreSqlHintConverter instance;

    protected PostgreSqlHintConverter() {
    }

    public static PostgreSqlHintConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlHintConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlHintConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SqlHint obj, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
