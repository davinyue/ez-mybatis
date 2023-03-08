package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFormulaUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateFieldItem;

public class DmFormulaUpdateFieldItemConverter extends OracleFormulaUpdateFieldItemConverter
        implements Converter<FormulaUpdateFieldItem> {
    private static volatile DmFormulaUpdateFieldItemConverter instance;

    protected DmFormulaUpdateFieldItemConverter() {
    }

    public static DmFormulaUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmFormulaUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmFormulaUpdateFieldItemConverter();
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
