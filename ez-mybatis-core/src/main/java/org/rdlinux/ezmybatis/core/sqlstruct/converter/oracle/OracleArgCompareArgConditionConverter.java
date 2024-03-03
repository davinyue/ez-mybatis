package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlArgCompareArgConditionConverter;
import org.rdlinux.ezmybatis.enumeration.Operator;

public class OracleArgCompareArgConditionConverter extends MySqlArgCompareArgConditionConverter
        implements Converter<ArgCompareArgCondition> {
    private static volatile OracleArgCompareArgConditionConverter instance;

    protected OracleArgCompareArgConditionConverter() {
    }

    public static OracleArgCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleArgCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleArgCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ArgCompareArgCondition obj, MybatisParamHolder mybatisParamHolder) {

        Operator operator = obj.getOperator();
        if (operator == Operator.regexp) {
            Operand leftValue = obj.getLeftValue();
            if (leftValue instanceof EntityField) {
                EzMybatisContent.setCurrentAccessField((EntityField) leftValue);
            }
            Converter<? extends Operand> leftConverter = EzMybatisContent.getConverter(configuration, leftValue.getClass());
            StringBuilder leftSql = new StringBuilder();
            leftSql.append(" REGEXP_LIKE(").append(leftConverter.buildSql(type, new StringBuilder(), configuration, leftValue,
                    mybatisParamHolder)).append(", ");
            Operand value = obj.getRightValue();
            Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                    value.getClass());
            leftSql.append(argConverter.buildSql(type, new StringBuilder(), configuration, value, mybatisParamHolder))
                    .append(") ");
            return sqlBuilder.append(leftSql);
        } else {
            return super.doBuildSql(type, sqlBuilder, configuration, obj, mybatisParamHolder);
        }

    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
