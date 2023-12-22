package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.EzQueryArg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlArgCompareArgConditionConverter extends AbstractConverter<ArgCompareArgCondition>
        implements Converter<ArgCompareArgCondition> {
    private static volatile MySqlArgCompareArgConditionConverter instance;

    protected MySqlArgCompareArgConditionConverter() {
    }

    public static MySqlArgCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlArgCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlArgCompareArgConditionConverter();
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
                                       ArgCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {
        Arg leftValue = obj.getLeftValue();
        Converter<? extends Arg> leftConverter = EzMybatisContent.getConverter(configuration, leftValue.getClass());
        StringBuilder leftSql = new StringBuilder();
        leftSql.append(" ").append(leftConverter.buildSql(type, new StringBuilder(), configuration, leftValue,
                mybatisParamHolder)).append(" ");
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
                                      Configuration configuration, ArgCompareArgCondition obj,
                                      MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        Arg value = obj.getRightValue();
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
                                  Configuration configuration, ArgCompareArgCondition obj,
                                  MybatisParamHolder mybatisParamHolder) {
        Operator operator = obj.getOperator();
        if (obj.getRightValues().size() == 1) {
            if (!(obj.getRightValues().get(0) instanceof EzQueryArg)) {
                if (operator == Operator.in) {
                    operator = Operator.eq;
                } else {
                    operator = Operator.ne;
                }
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
        for (int i = 0; i < obj.getRightValues().size(); i++) {
            Arg arg = obj.getRightValues().get(i);
            Converter<? extends Arg> argConverter = EzMybatisContent.getConverter(configuration,
                    arg.getClass());
            sqlBuilder.append(argConverter.buildSql(type, new StringBuilder(), configuration, arg,
                    mybatisParamHolder));
            if (i + 1 < obj.getRightValues().size()) {
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
                                         Configuration configuration, ArgCompareArgCondition obj,
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

    private StringBuilder isNullBuild(StringBuilder leftSql, StringBuilder sqlBuilder, ArgCompareArgCondition obj) {
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
