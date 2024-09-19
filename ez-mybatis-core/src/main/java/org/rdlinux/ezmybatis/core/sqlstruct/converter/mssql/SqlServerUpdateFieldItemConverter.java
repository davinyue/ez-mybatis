package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

public class SqlServerUpdateFieldItemConverter extends MySqlUpdateFieldItemConverter implements Converter<UpdateFieldItem> {
    private static volatile SqlServerUpdateFieldItemConverter instance;

    protected SqlServerUpdateFieldItemConverter() {
    }

    public static SqlServerUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new SqlServerUpdateFieldItemConverter();
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
        return DbType.SQL_SERVER;
    }
}
