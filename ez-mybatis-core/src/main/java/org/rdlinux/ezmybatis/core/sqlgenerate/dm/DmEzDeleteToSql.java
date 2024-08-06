package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleEzDeleteToSql;

public class DmEzDeleteToSql extends OracleEzDeleteToSql {
    private static volatile DmEzDeleteToSql instance;

    private DmEzDeleteToSql() {
    }

    public static DmEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (DmEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new DmEzDeleteToSql();
                }
            }
        }
        return instance;
    }
}
