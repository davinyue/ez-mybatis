package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.AliasArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlAliasArgConverter extends AbstractConverter<AliasArg> implements Converter<AliasArg> {
    private static volatile MySqlAliasArgConverter instance;

    protected MySqlAliasArgConverter() {
    }

    public static MySqlAliasArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlAliasArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlAliasArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       AliasArg obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return sqlBuilder.append(keywordQM).append(obj.getAlias()).append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
