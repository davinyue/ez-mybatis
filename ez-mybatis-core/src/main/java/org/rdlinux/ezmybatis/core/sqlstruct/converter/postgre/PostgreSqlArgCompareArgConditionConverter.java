package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlArgCompareArgConditionConverter;
import org.rdlinux.ezmybatis.enumeration.Operator;

public class PostgreSqlArgCompareArgConditionConverter extends MySqlArgCompareArgConditionConverter
        implements Converter<ArgCompareArgCondition> {
    private static volatile PostgreSqlArgCompareArgConditionConverter instance;

    protected PostgreSqlArgCompareArgConditionConverter() {
    }

    public static PostgreSqlArgCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlArgCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlArgCompareArgConditionConverter();
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
