package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.MultipleRetOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.enumeration.Operator;

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
    protected void doBuildSql(Type type, ArgCompareArgCondition obj, SqlGenerateContext sqlGenerateContext) {
        Operand leftValue = obj.getLeftValue();
        if (leftValue instanceof EntityField) {
            sqlGenerateContext.pushAccessField((EntityField) leftValue);
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        Configuration configuration = sqlGenerateContext.getConfiguration();
        Converter<? extends Operand> leftConverter = EzMybatisContent.getConverter(configuration, leftValue.getClass());
        StringBuilder leftSql = new StringBuilder(" ");
        SqlGenerateContext leftSqlContent = SqlGenerateContext.copyOf(sqlGenerateContext, leftSql);
        leftConverter.buildSql(type, leftValue, leftSqlContent);
        leftSql.append(" ");
        Operator operator = obj.getOperator();
        StringBuilder ret;
        if (operator == Operator.isNull || operator == Operator.isNotNull) {
            ret = this.isNullBuild(leftSql, sqlBuilder, obj);
        } else if (operator == Operator.between || operator == Operator.notBetween) {
            ret = this.isBetweenBuild(leftSql, type, obj, sqlGenerateContext);
        } else if (operator == Operator.in || operator == Operator.notIn) {
            ret = this.inBuild(leftSql, type, obj, sqlGenerateContext);
        } else {
            ret = this.normalBuild(leftSql, type, obj, sqlGenerateContext);
        }
        if (leftValue instanceof EntityField) {
            sqlGenerateContext.popAccessField();
        }
        sqlGenerateContext.setSqlBuilder(ret);
    }

    private StringBuilder normalBuild(StringBuilder leftSql, Type type, ArgCompareArgCondition obj,
                                      SqlGenerateContext sqlGenerateContext) {
        Operator operator = obj.getOperator();
        Operand value = obj.getRightValue();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        Configuration configuration = sqlGenerateContext.getConfiguration();
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                value.getClass());
        SqlGenerateContext argSqlCt = SqlGenerateContext.copyOf(sqlGenerateContext);
        argConverter.buildSql(type, value, argSqlCt);
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(argSqlCt.getSqlBuilder())
                .append(" ");
    }

    private StringBuilder inBuild(StringBuilder leftSql, Type type, ArgCompareArgCondition obj,
                                  SqlGenerateContext sqlGenerateContext) {
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        Configuration configuration = sqlGenerateContext.getConfiguration();
        Operator operator = obj.getOperator();
        if (obj.getRightValues().size() == 1) {
            if (!(obj.getRightValues().get(0) instanceof MultipleRetOperand)) {
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
            Operand arg = obj.getRightValues().get(i);
            Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                    arg.getClass());
            SqlGenerateContext argSqlCt = SqlGenerateContext.copyOf(sqlGenerateContext);
            argConverter.buildSql(type, arg, argSqlCt);
            sqlBuilder.append(argSqlCt.getSqlBuilder());
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

    private StringBuilder isBetweenBuild(StringBuilder leftSql, Type type, ArgCompareArgCondition obj,
                                         SqlGenerateContext sqlGenerateContext) {
        Operator operator = obj.getOperator();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        Configuration configuration = sqlGenerateContext.getConfiguration();

        Converter<? extends Operand> minArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMinValue().getClass());
        Converter<? extends Operand> maxArgConverter = EzMybatisContent.getConverter(configuration,
                obj.getMaxValue().getClass());

        SqlGenerateContext minSqlCt = SqlGenerateContext.copyOf(sqlGenerateContext);
        minArgConverter.buildSql(type, obj.getMinValue(), minSqlCt);

        SqlGenerateContext maxSqlCt = SqlGenerateContext.copyOf(sqlGenerateContext);
        maxArgConverter.buildSql(type, obj.getMaxValue(), maxSqlCt);
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ")
                .append(minSqlCt.getSqlBuilder())
                .append(" AND ")
                .append(maxSqlCt.getSqlBuilder()).append(" ");
    }

    private StringBuilder isNullBuild(StringBuilder leftSql, StringBuilder sqlBuilder, ArgCompareArgCondition obj) {
        Operator operator = obj.getOperator();
        return sqlBuilder.append(" ")
                .append(leftSql)
                .append(" ")
                .append(this.getOperatorStr(operator))
                .append(" ");
    }

}
