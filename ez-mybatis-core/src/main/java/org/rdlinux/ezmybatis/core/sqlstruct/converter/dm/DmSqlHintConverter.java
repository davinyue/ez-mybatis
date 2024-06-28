package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSqlHintConverter;

public class DmSqlHintConverter extends OracleSqlHintConverter implements Converter<SqlHint> {
    private static volatile DmSqlHintConverter instance;

    protected DmSqlHintConverter() {
    }

    public static DmSqlHintConverter getInstance() {
        if (instance == null) {
            synchronized (DmSqlHintConverter.class) {
                if (instance == null) {
                    instance = new DmSqlHintConverter();
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
