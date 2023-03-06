package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFunFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FunFormulaElement;

public class DmFunFormulaElementConverter extends OracleFunFormulaElementConverter
        implements Converter<FunFormulaElement> {
    private static volatile DmFunFormulaElementConverter instance;

    protected DmFunFormulaElementConverter() {
    }

    public static DmFunFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmFunFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmFunFormulaElementConverter();
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
