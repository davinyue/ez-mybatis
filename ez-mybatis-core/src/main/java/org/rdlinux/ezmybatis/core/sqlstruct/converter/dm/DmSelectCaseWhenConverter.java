package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectCaseWhenConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectCaseWhen;

public class DmSelectCaseWhenConverter extends OracleSelectCaseWhenConverter implements Converter<SelectCaseWhen> {
    private static volatile DmSelectCaseWhenConverter instance;

    protected DmSelectCaseWhenConverter() {
    }

    public static DmSelectCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new DmSelectCaseWhenConverter();
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
