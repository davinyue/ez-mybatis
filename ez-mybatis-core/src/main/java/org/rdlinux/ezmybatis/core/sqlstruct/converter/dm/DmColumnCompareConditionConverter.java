package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleColumnCompareConditionConverter;

public class DmColumnCompareConditionConverter extends OracleColumnCompareConditionConverter {
    private static volatile DmColumnCompareConditionConverter instance;

    protected DmColumnCompareConditionConverter() {
    }

    public static DmColumnCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmColumnCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new DmColumnCompareConditionConverter();
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
