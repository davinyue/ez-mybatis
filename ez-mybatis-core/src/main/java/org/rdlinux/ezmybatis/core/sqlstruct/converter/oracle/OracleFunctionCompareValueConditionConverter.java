package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionCompareValueConditionConverter;

public class OracleFunctionCompareValueConditionConverter extends MySqlFunctionCompareValueConditionConverter
        implements Converter<FunctionCompareValueCondition> {
    private static volatile OracleFunctionCompareValueConditionConverter instance;

    protected OracleFunctionCompareValueConditionConverter() {
    }

    public static OracleFunctionCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunctionCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleFunctionCompareValueConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
