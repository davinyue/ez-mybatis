package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy.GroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlGroupItemConverter;

public class OracleGroupItemConverter extends MySqlGroupItemConverter implements Converter<GroupItem> {
    private static volatile OracleGroupItemConverter instance;

    protected OracleGroupItemConverter() {
    }

    public static OracleGroupItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleGroupItemConverter.class) {
                if (instance == null) {
                    instance = new OracleGroupItemConverter();
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
