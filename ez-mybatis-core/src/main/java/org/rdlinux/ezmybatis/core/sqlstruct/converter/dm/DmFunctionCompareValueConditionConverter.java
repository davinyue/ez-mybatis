package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFunctionCompareValueConditionConverter;

public class DmFunctionCompareValueConditionConverter extends OracleFunctionCompareValueConditionConverter
        implements Converter<FunctionCompareValueCondition> {
    private static volatile DmFunctionCompareValueConditionConverter instance;

    protected DmFunctionCompareValueConditionConverter() {
    }

    public static DmFunctionCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmFunctionCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new DmFunctionCompareValueConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
