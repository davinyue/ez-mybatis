package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleColumnFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ColumnFormulaElement;

public class DmColumnFormulaElementConverter extends OracleColumnFormulaElementConverter
        implements Converter<ColumnFormulaElement> {
    private static volatile DmColumnFormulaElementConverter instance;

    protected DmColumnFormulaElementConverter() {
    }

    public static DmColumnFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmColumnFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmColumnFormulaElementConverter();
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
