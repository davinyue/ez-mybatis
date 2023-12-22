package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionCompareArgConditionConverter;

public class PostgreSqlFunctionCompareArgConditionConverter extends MySqlFunctionCompareArgConditionConverter
        implements Converter<FunctionCompareArgCondition> {
    private static volatile PostgreSqlFunctionCompareArgConditionConverter instance;

    protected PostgreSqlFunctionCompareArgConditionConverter() {
    }

    public static PostgreSqlFunctionCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlFunctionCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlFunctionCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getOperatorStr(Operator operator) {
        if (Operator.regexp == operator) {
            return "~";
        }
        return super.getOperatorStr(operator);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
