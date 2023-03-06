package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleValueFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ValueFormulaElement;

public class DmValueFormulaElementConverter extends OracleValueFormulaElementConverter
        implements Converter<ValueFormulaElement> {
    private static volatile DmValueFormulaElementConverter instance;

    protected DmValueFormulaElementConverter() {
    }

    public static DmValueFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmValueFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmValueFormulaElementConverter();
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
