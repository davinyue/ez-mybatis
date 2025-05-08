package org.rdlinux.ezmybatis.mp.entity;

import org.rdlinux.ezmybatis.constant.DbType;

public class MpPostgreSqlEntityInfoBuilder extends MpMySqlEntityInfoBuilder {
    private static volatile MpPostgreSqlEntityInfoBuilder instance;

    protected MpPostgreSqlEntityInfoBuilder() {
    }

    public static MpPostgreSqlEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MpPostgreSqlEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MpPostgreSqlEntityInfoBuilder();
                }
            }
        }
        return instance;
    }


    @Override
    public DbType getSupportedDbType() {
        return DbType.POSTGRE_SQL;
    }
}
