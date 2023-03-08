package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectFormulaConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFormula;

public class DmSelectFormulaConverter extends OracleSelectFormulaConverter implements Converter<SelectFormula> {
    private static volatile DmSelectFormulaConverter instance;

    protected DmSelectFormulaConverter() {
    }

    public static DmSelectFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectFormulaConverter.class) {
                if (instance == null) {
                    instance = new DmSelectFormulaConverter();
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
