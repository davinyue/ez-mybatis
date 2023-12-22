package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.CaseWhenCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlCaseWhenCompareArgConditionConverter extends AbstractConverter<CaseWhenCompareArgCondition>
        implements Converter<CaseWhenCompareArgCondition> {
    private static volatile PostgreSqlCaseWhenCompareArgConditionConverter instance;

    protected PostgreSqlCaseWhenCompareArgConditionConverter() {
    }

    public static PostgreSqlCaseWhenCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlCaseWhenCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlCaseWhenCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        CaseWhen caseWhen = obj.getCaseWhen();
        Converter<? extends CaseWhen> formulaConverter = EzMybatisContent.getConverter(configuration,
                caseWhen.getClass());
        StringBuilder leftSql = new StringBuilder();
        leftSql.append(" ").append(formulaConverter.buildSql(type, new StringBuilder(), configuration, caseWhen,
                mybatisParamHolder)).append(" ");
        return PostgreSqlFunctionCompareArgConditionConverter.getInstance()
                .doBuildSql(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
