package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFormulaFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaFormulaElement;

public class DmFormulaFormulaElementConverter extends OracleFormulaFormulaElementConverter
        implements Converter<FormulaFormulaElement> {
    private static volatile DmFormulaFormulaElementConverter instance;

    protected DmFormulaFormulaElementConverter() {
    }

    public static DmFormulaFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmFormulaFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmFormulaFormulaElementConverter();
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
