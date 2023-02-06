package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleFieldCompareConditionConverter;

public class DmFieldCompareConditionConverter extends OracleFieldCompareConditionConverter {
    private static volatile DmFieldCompareConditionConverter instance;

    protected DmFieldCompareConditionConverter() {
    }

    public static DmFieldCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmFieldCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new DmFieldCompareConditionConverter();
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
