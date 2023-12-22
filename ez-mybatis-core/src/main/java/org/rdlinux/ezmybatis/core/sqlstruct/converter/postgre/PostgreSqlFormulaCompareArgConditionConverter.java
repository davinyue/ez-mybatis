package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaCompareArgConditionConverter;

public class PostgreSqlFormulaCompareArgConditionConverter extends MySqlFormulaCompareArgConditionConverter
        implements Converter<FormulaCompareArgCondition> {
    private static volatile PostgreSqlFormulaCompareArgConditionConverter instance;

    protected PostgreSqlFormulaCompareArgConditionConverter() {
    }

    public static PostgreSqlFormulaCompareArgConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlFormulaCompareArgConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlFormulaCompareArgConditionConverter();
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
