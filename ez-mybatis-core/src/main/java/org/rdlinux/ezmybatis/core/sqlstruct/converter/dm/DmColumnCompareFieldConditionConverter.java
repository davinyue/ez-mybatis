package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleColumnCompareFieldConditionConverter;

public class DmColumnCompareFieldConditionConverter extends OracleColumnCompareFieldConditionConverter {
    private static volatile DmColumnCompareFieldConditionConverter instance;

    protected DmColumnCompareFieldConditionConverter() {
    }

    public static DmColumnCompareFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (DmColumnCompareFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new DmColumnCompareFieldConditionConverter();
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
