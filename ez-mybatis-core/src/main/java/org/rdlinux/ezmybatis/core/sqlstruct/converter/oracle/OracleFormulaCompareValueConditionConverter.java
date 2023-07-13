package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaCompareValueConditionConverter;

public class OracleFormulaCompareValueConditionConverter extends MySqlFormulaCompareValueConditionConverter
        implements Converter<FormulaCompareValueCondition> {
    private static volatile OracleFormulaCompareValueConditionConverter instance;

    protected OracleFormulaCompareValueConditionConverter() {
    }

    public static OracleFormulaCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFormulaCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new OracleFormulaCompareValueConditionConverter();
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
