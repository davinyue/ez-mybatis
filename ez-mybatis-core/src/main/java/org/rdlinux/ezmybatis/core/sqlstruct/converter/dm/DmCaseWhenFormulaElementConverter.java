package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleCaseWhenFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.CaseWhenFormulaElement;

public class DmCaseWhenFormulaElementConverter extends OracleCaseWhenFormulaElementConverter
        implements Converter<CaseWhenFormulaElement> {
    private static volatile DmCaseWhenFormulaElementConverter instance;

    protected DmCaseWhenFormulaElementConverter() {
    }

    public static DmCaseWhenFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmCaseWhenFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmCaseWhenFormulaElementConverter();
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
