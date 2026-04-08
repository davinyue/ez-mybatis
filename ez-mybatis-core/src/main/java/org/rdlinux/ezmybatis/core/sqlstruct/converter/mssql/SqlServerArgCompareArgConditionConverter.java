package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlArgCompareArgConditionConverter;
import org.rdlinux.ezmybatis.enumeration.Operator;

public class SqlServerArgCompareArgConditionConverter extends MySqlArgCompareArgConditionConverter
        implements Converter<ArgCompareArgCondition> {
    private static volatile SqlServerArgCompareArgConditionConverter instance;

    protected SqlServerArgCompareArgConditionConverter() {
    }

    public static SqlServerArgCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerArgCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new SqlServerArgCompareArgConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getOperatorStr(Operator operator) {
        if (Operator.regexp == operator) {
            return "LIKE";
        }
        return super.getOperatorStr(operator);
    }


}
