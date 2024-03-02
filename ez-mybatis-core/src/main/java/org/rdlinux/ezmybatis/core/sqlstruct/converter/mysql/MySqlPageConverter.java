package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlPageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile MySqlPageConverter instance;

    protected MySqlPageConverter() {
    }

    public static MySqlPageConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlPageConverter.class) {
                if (instance == null) {
                    instance = new MySqlPageConverter();
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
        return sqlBuilder.append(" LIMIT ").append(limit.getSkip()).append(", ").append(limit.getSize()).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
