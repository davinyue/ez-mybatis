package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

public class PostgreSqlUpdateFieldItemConverter extends MySqlUpdateFieldItemConverter implements Converter<UpdateFieldItem> {
    private static volatile PostgreSqlUpdateFieldItemConverter instance;

    protected PostgreSqlUpdateFieldItemConverter() {
    }

    public static PostgreSqlUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected boolean appendAlias() {
        return false;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
