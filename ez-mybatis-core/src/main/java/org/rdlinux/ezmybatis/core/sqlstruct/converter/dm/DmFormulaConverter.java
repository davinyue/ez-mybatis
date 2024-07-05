package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFormulaConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

public class DmFormulaConverter extends OracleFormulaConverter implements Converter<Formula> {
    private static volatile DmFormulaConverter instance;

    protected DmFormulaConverter() {
    }

    public static DmFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (DmFormulaConverter.class) {
                if (instance == null) {
                    instance = new DmFormulaConverter();
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
