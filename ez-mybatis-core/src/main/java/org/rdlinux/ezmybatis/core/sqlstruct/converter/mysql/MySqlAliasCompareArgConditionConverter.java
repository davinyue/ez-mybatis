package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.AliasCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlAliasCompareArgConditionConverter extends AbstractConverter<AliasCompareArgCondition>
        implements Converter<AliasCompareArgCondition> {
    private static volatile MySqlAliasCompareArgConditionConverter instance;

    protected MySqlAliasCompareArgConditionConverter() {
    }

    public static MySqlAliasCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlAliasCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlAliasCompareArgConditionConverter();
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
        return MySqlFunctionCompareArgConditionConverter.getInstance()
                .doBuildSql(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
