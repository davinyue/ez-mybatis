package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.KeywordsArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlKeywordsArgConverter extends AbstractConverter<KeywordsArg> implements Converter<KeywordsArg> {
    private static volatile MySqlKeywordsArgConverter instance;

    protected MySqlKeywordsArgConverter() {
    }

    public static MySqlKeywordsArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       KeywordsArg obj, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder.append(obj.getKeywords());
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
