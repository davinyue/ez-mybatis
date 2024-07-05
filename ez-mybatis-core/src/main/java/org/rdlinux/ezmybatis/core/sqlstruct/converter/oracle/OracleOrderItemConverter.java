package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlOrderItemConverter;

public class OracleOrderItemConverter extends MySqlOrderItemConverter implements Converter<OrderItem> {
    private static volatile OracleOrderItemConverter instance;

    protected OracleOrderItemConverter() {
    }

    public static OracleOrderItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleOrderItemConverter.class) {
                if (instance == null) {
                    instance = new OracleOrderItemConverter();
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
