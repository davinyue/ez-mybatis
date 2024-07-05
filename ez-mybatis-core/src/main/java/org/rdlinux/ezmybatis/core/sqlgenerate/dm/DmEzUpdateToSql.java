package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleEzUpdateToSql;

public class DmEzUpdateToSql extends OracleEzUpdateToSql {
    private static volatile DmEzUpdateToSql instance;

    private DmEzUpdateToSql() {
    }

    public static DmEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (DmEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new DmEzUpdateToSql();
                }
            }
        }
        return instance;
    }
}
