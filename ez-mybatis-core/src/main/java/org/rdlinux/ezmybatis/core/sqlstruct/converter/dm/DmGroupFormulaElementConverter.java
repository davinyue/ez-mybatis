package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleGroupFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.GroupFormulaElement;

public class DmGroupFormulaElementConverter extends OracleGroupFormulaElementConverter
        implements Converter<GroupFormulaElement> {
    private static volatile DmGroupFormulaElementConverter instance;

    protected DmGroupFormulaElementConverter() {
    }

    public static DmGroupFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmGroupFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmGroupFormulaElementConverter();
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
