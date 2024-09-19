package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;

public class SqlServerUpdateColumnItemConverter extends MySqlUpdateColumnItemConverter implements Converter<UpdateColumnItem> {
    private static volatile SqlServerUpdateColumnItemConverter instance;

    protected SqlServerUpdateColumnItemConverter() {
    }

    public static SqlServerUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new SqlServerUpdateColumnItemConverter();
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
