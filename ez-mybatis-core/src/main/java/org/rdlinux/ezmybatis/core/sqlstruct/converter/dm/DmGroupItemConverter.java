package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy.GroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleGroupItemConverter;

public class DmGroupItemConverter extends OracleGroupItemConverter implements Converter<GroupItem> {
    private static volatile DmGroupItemConverter instance;

    protected DmGroupItemConverter() {
    }

    public static DmGroupItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmGroupItemConverter.class) {
                if (instance == null) {
                    instance = new DmGroupItemConverter();
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
