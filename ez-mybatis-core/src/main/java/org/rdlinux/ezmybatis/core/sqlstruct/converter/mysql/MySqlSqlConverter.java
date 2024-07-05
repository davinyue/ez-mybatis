package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Sql;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlSqlConverter extends AbstractConverter<Sql> implements Converter<Sql> {
    private static volatile MySqlSqlConverter instance;

    protected MySqlSqlConverter() {
    }

    public static MySqlSqlConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       Sql obj, MybatisParamHolder mybatisParamHolder) {

        return sqlBuilder.append("(").append(obj.getSql()).append(")");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
