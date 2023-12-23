package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Sql;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlSqlArgConverter extends AbstractConverter<Sql> implements Converter<Sql> {
    private static volatile MySqlSqlArgConverter instance;

    protected MySqlSqlArgConverter() {
    }

    public static MySqlSqlArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlArgConverter();
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
