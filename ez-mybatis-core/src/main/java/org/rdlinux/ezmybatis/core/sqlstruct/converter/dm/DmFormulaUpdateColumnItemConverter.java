package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFormulaUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateColumnItem;

public class DmFormulaUpdateColumnItemConverter extends OracleFormulaUpdateColumnItemConverter
        implements Converter<FormulaUpdateColumnItem> {
    private static volatile DmFormulaUpdateColumnItemConverter instance;

    protected DmFormulaUpdateColumnItemConverter() {
    }

    public static DmFormulaUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmFormulaUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmFormulaUpdateColumnItemConverter();
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
