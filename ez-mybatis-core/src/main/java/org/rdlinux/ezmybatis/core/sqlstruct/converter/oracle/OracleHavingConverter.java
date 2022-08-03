package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.Having;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlHavingConverter;

public class OracleHavingConverter extends MySqlHavingConverter implements Converter<Having> {
    private static volatile OracleHavingConverter instance;

    protected OracleHavingConverter() {
    }

    public static OracleHavingConverter getInstance() {
        if (instance == null) {
            synchronized (OracleHavingConverter.class) {
                if (instance == null) {
                    instance = new OracleHavingConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
