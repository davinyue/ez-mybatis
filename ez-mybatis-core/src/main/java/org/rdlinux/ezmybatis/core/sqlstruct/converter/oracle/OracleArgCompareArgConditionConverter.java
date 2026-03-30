package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, ArgCompareArgCondition obj, SqlGenerateContext sqlGenerateContext) {
        Operator operator = obj.getOperator();
        if (operator == Operator.regexp) {
            Operand leftValue = obj.getLeftValue();
            if (leftValue instanceof EntityField) {
                sqlGenerateContext.pushAccessField((EntityField) leftValue);
            }
            Configuration configuration = sqlGenerateContext.getConfiguration();
            Converter<? extends Operand> leftConverter = EzMybatisContent.getConverter(configuration,
                    leftValue.getClass());
            StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
            sqlBuilder.append(" REGEXP_LIKE(");
            leftConverter.buildSql(type, leftValue, sqlGenerateContext);
            sqlBuilder.append(", ");
            Operand value = obj.getRightValue();
            Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                    value.getClass());
            argConverter.buildSql(type, value, sqlGenerateContext);
            sqlBuilder.append(") ");
        } else {
            super.doBuildSql(type, obj, sqlGenerateContext);
        }

    }

}
