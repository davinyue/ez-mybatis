package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleOrderItemConverter;

public class DmOrderItemConverter extends OracleOrderItemConverter implements Converter<OrderItem> {
    private static volatile DmOrderItemConverter instance;

    protected DmOrderItemConverter() {
    }

    public static DmOrderItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmOrderItemConverter.class) {
                if (instance == null) {
                    instance = new DmOrderItemConverter();
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
