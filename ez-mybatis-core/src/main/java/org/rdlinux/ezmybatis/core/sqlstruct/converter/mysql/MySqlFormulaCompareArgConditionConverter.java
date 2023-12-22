package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
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

    protected String getOperatorStr(Operator operator) {
        return operator.getOperator();
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        if (operator == Operator.isNull || operator == Operator.isNotNull) {
            return this.isNullBuild(type, sqlBuilder, configuration, obj, mybatisParamHolder);
        } else if (operator == Operator.between || operator == Operator.notBetween) {
            return this.isBetweenBuild(type, sqlBuilder, configuration, obj, mybatisParamHolder);
        } else if (operator == Operator.in || operator == Operator.notIn) {
            return this.inBuild(type, sqlBuilder, configuration, obj, mybatisParamHolder);
        } else {
            return this.normalBuild(type, sqlBuilder, configuration, obj, mybatisParamHolder);
        }
    }

    private StringBuilder normalBuild(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                      FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Formula formula = obj.getFormula();
        Operator operator = obj.getOperator();
        Arg value = obj.getValue();
        Converter<? extends Formula> formulaConverter = EzMybatisContent.getConverter(configuration,
                formula.getClass());
        Converter<? extends Arg> argConverter = EzMybatisContent.getConverter(configuration,
                value.getClass());
        return sqlBuilder.append(" ")
                .append(formulaConverter.buildSql(type, new StringBuilder(), configuration, formula, mybatisParamHolder))
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(argConverter.buildSql(type, new StringBuilder(), configuration, value, mybatisParamHolder))
                .append(" ");
    }

    private StringBuilder inBuild(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                  FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        Converter<? extends Formula> formulaConverter = EzMybatisContent.getConverter(configuration,
                obj.getFormula().getClass());
        if (obj.getValues().size() == 1) {
            if (operator == Operator.in) {
                operator = Operator.eq;
            } else {
                operator = Operator.ne;
            }
        }
        sqlBuilder.append(" ")
                .append(formulaConverter.buildSql(type, new StringBuilder(), configuration, obj.getFormula(),
                        mybatisParamHolder))
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ");
        if (operator == Operator.in || operator == Operator.notIn) {
            sqlBuilder.append("(");
        }
        for (int i = 0; i < obj.getValues().size(); i++) {
            Arg arg = obj.getValues().get(i);
            Converter<? extends Arg> argConverter = EzMybatisContent.getConverter(configuration,
                    arg.getClass());
            sqlBuilder.append(argConverter.buildSql(type, new StringBuilder(), configuration, arg,
                    mybatisParamHolder));
            if (i + 1 < obj.getValues().size()) {
                sqlBuilder.append(", ");
            }
        }
        if (operator == Operator.in || operator == Operator.notIn) {
            sqlBuilder.append(")");
        }
        sqlBuilder.append(" ");
        return sqlBuilder;
    }

    private StringBuilder isBetweenBuild(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                         FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Formula formula = obj.getFormula();
        Operator operator = obj.getOperator();
        Converter<? extends Formula> formulaConverter = EzMybatisContent.getConverter(configuration,
                formula.getClass());
        Converter<? extends Arg> minArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMinValue().getClass());
        Converter<? extends Arg> maxArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMaxValue().getClass());
        return sqlBuilder.append(" ")
                .append(formulaConverter.buildSql(type, new StringBuilder(), configuration, formula,
                        mybatisParamHolder))
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(minArgConverter.buildSql(type, new StringBuilder(), configuration, obj.getMinValue(),
                        mybatisParamHolder))
                .append(" AND ")
                .append(maxArgConverter.buildSql(type, new StringBuilder(), configuration, obj.getMaxValue(),
                        mybatisParamHolder)).append(" ");
    }

    private StringBuilder isNullBuild(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                      FormulaCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Formula formula = obj.getFormula();
        Operator operator = obj.getOperator();
        Converter<? extends Formula> formulaConverter = EzMybatisContent.getConverter(configuration,
                formula.getClass());
        return sqlBuilder.append(" ")
                .append(formulaConverter.buildSql(type, new StringBuilder(), configuration, formula, mybatisParamHolder))
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
