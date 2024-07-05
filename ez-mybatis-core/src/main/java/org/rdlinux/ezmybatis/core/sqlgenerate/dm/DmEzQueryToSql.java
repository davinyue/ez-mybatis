package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;

public class DmEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile DmEzQueryToSql instance;

    private DmEzQueryToSql() {
    }

    public static DmEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (DmEzQueryToSql.class) {
                if (instance == null) {
                    instance = new DmEzQueryToSql();
                }
            }
        }
        return instance;
    }
}
