package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectTableAllItemConverter;

public class DmSelectTableAllItemConverter extends OracleSelectTableAllItemConverter {
    private static volatile DmSelectTableAllItemConverter instance;

    protected DmSelectTableAllItemConverter() {
    }

    public static DmSelectTableAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectTableAllItemConverter.class) {
                if (instance == null) {
                    instance = new DmSelectTableAllItemConverter();
                }
            }
        }
        return instance;
    }

}
