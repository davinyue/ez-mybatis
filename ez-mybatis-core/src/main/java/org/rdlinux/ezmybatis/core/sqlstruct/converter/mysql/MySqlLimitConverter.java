package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile MySqlLimitConverter instance;

    protected MySqlLimitConverter() {
    }

    public static MySqlLimitConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlLimitConverter.class) {
                if (instance == null) {
                    instance = new MySqlLimitConverter();
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
        return sqlBuilder.append(" LIMIT ").append(limit.getSize()).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
