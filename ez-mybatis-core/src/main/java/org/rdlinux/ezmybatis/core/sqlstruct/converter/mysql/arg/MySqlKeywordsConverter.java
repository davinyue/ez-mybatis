package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlKeywordsConverter extends AbstractConverter<Keywords> implements Converter<Keywords> {
    private static volatile MySqlKeywordsConverter instance;

    protected MySqlKeywordsConverter() {
    }

    public static MySqlKeywordsConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       Keywords obj, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder.append(obj.getKeywords());
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
