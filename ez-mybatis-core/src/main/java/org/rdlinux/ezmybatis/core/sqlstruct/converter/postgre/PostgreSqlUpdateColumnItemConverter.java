package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;

public class PostgreSqlUpdateColumnItemConverter extends MySqlUpdateColumnItemConverter implements Converter<UpdateColumnItem> {
    private static volatile PostgreSqlUpdateColumnItemConverter instance;

    protected PostgreSqlUpdateColumnItemConverter() {
    }

    public static PostgreSqlUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlUpdateColumnItemConverter();
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
