package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.AliasCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlAliasCompareArgConditionConverter extends AbstractConverter<AliasCompareArgCondition>
        implements Converter<AliasCompareArgCondition> {
    private static volatile PostgreSqlAliasCompareArgConditionConverter instance;

    protected PostgreSqlAliasCompareArgConditionConverter() {
    }

    public static PostgreSqlAliasCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlAliasCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlAliasCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       AliasCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        StringBuilder leftSql = new StringBuilder();
        leftSql.append(" ")
                .append(keywordQM).append(obj.getAlias()).append(keywordQM)
                .append(" ");
        return PostgreSqlFunctionCompareArgConditionConverter.getInstance()
                .doBuildSql(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
