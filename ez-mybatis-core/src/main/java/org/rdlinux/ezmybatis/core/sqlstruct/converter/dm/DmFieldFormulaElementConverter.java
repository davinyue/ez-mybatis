package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFieldFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FieldFormulaElement;

public class DmFieldFormulaElementConverter extends OracleFieldFormulaElementConverter
        implements Converter<FieldFormulaElement> {
    private static volatile DmFieldFormulaElementConverter instance;

    protected DmFieldFormulaElementConverter() {
    }

    public static DmFieldFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmFieldFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmFieldFormulaElementConverter();
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
