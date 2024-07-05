package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSqlHintConverter;

public class OracleSqlHintConverter extends MySqlSqlHintConverter implements Converter<SqlHint> {
    private static volatile OracleSqlHintConverter instance;

    protected OracleSqlHintConverter() {
    }

    public static OracleSqlHintConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSqlHintConverter.class) {
                if (instance == null) {
                    instance = new OracleSqlHintConverter();
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
