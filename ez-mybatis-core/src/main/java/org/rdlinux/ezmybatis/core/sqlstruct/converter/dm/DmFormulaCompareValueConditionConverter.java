package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFormulaCompareValueConditionConverter;

public class DmFormulaCompareValueConditionConverter extends OracleFormulaCompareValueConditionConverter
        implements Converter<FormulaCompareValueCondition> {
    private static volatile DmFormulaCompareValueConditionConverter instance;

    protected DmFormulaCompareValueConditionConverter() {
    }

    public static DmFormulaCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmFormulaCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new DmFormulaCompareValueConditionConverter();
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
