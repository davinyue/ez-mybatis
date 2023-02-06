package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFieldCompareColumnConditionConverter;

public class DmFieldCompareColumnConditionConverter extends OracleFieldCompareColumnConditionConverter {
    private static volatile DmFieldCompareColumnConditionConverter instance;

    protected DmFieldCompareColumnConditionConverter() {
    }

    public static DmFieldCompareColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmFieldCompareColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new DmFieldCompareColumnConditionConverter();
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
