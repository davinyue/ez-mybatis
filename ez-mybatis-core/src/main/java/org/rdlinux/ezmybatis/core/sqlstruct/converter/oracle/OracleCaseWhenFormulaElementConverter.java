package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlCaseWhenFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.CaseWhenFormulaElement;

public class OracleCaseWhenFormulaElementConverter extends MySqlCaseWhenFormulaElementConverter
        implements Converter<CaseWhenFormulaElement> {
    private static volatile OracleCaseWhenFormulaElementConverter instance;

    protected OracleCaseWhenFormulaElementConverter() {
    }

    public static OracleCaseWhenFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleCaseWhenFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleCaseWhenFormulaElementConverter();
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
