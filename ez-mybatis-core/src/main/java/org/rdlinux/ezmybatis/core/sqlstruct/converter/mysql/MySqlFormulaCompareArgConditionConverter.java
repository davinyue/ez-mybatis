package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

public class MySqlFormulaCompareArgConditionConverter extends AbstractConverter<FormulaCompareArgCondition>
        implements Converter<FormulaCompareArgCondition> {
    private static volatile MySqlFormulaCompareArgConditionConverter instance;

    protected MySqlFormulaCompareArgConditionConverter() {
    }

    public static MySqlFormulaCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Formula formula = obj.getFormula();
        Converter<? extends Formula> formulaConverter = EzMybatisContent.getConverter(configuration,
                formula.getClass());
        StringBuilder leftSql = new StringBuilder();
        leftSql.append(" ").append(formulaConverter.buildSql(type, new StringBuilder(), configuration, formula,
                mybatisParamHolder)).append(" ");
        return MySqlFunctionCompareArgConditionConverter.getInstance()
                .doBuildSql(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
