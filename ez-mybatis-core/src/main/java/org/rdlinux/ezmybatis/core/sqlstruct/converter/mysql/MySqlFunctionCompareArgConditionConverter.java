package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.CompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlFunctionCompareArgConditionConverter extends AbstractConverter<FunctionCompareArgCondition>
        implements Converter<FunctionCompareArgCondition> {
    private static volatile MySqlFunctionCompareArgConditionConverter instance;

    protected MySqlFunctionCompareArgConditionConverter() {
    }

    public static MySqlFunctionCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionCompareArgConditionConverter();
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
                                       FunctionCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Function function = obj.getFunction();
        Converter<? extends Function> formulaConverter = EzMybatisContent.getConverter(configuration,
                function.getClass());
        StringBuilder leftSql = new StringBuilder();
        leftSql.append(" ").append(formulaConverter.buildSql(type, new StringBuilder(), configuration, function,
                mybatisParamHolder)).append(" ");
        return this.doBuildSql(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
    }

    public StringBuilder doBuildSql(StringBuilder leftSql, Type type, StringBuilder sqlBuilder,
                                    Configuration configuration, CompareArgCondition obj,
                                    MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        if (operator == Operator.isNull || operator == Operator.isNotNull) {
            return this.isNullBuild(leftSql, sqlBuilder, obj);
        } else if (operator == Operator.between || operator == Operator.notBetween) {
            return this.isBetweenBuild(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
        } else if (operator == Operator.in || operator == Operator.notIn) {
            return this.inBuild(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
        } else {
            return this.normalBuild(leftSql, type, sqlBuilder, configuration, obj, mybatisParamHolder);
        }
    }

    private StringBuilder normalBuild(StringBuilder leftSql, Type type, StringBuilder sqlBuilder,
                                      Configuration configuration, CompareArgCondition obj,
                                      MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        Arg value = obj.getValue();
        Converter<? extends Arg> argConverter = EzMybatisContent.getConverter(configuration,
                value.getClass());
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(argConverter.buildSql(type, new StringBuilder(), configuration, value, mybatisParamHolder))
                .append(" ");
    }

    private StringBuilder inBuild(StringBuilder leftSql, Type type, StringBuilder sqlBuilder,
                                  Configuration configuration, CompareArgCondition obj,
                                  MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        if (obj.getValues().size() == 1) {
            if (operator == Operator.in) {
                operator = Operator.eq;
            } else {
                operator = Operator.ne;
            }
        }
        sqlBuilder.append(" ")
                .append(leftSql)
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

    private StringBuilder isBetweenBuild(StringBuilder leftSql, Type type, StringBuilder sqlBuilder,
                                         Configuration configuration, CompareArgCondition obj,
                                         MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        Converter<? extends Arg> minArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMinValue().getClass());
        Converter<? extends Arg> maxArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMaxValue().getClass());
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(minArgConverter.buildSql(type, new StringBuilder(), configuration, obj.getMinValue(),
                        mybatisParamHolder))
                .append(" AND ")
                .append(maxArgConverter.buildSql(type, new StringBuilder(), configuration, obj.getMaxValue(),
                        mybatisParamHolder)).append(" ");
    }

    private StringBuilder isNullBuild(StringBuilder leftSql, StringBuilder sqlBuilder, CompareArgCondition obj) {
        Operator operator = obj.getOperator();
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
