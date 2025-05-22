package org.rdlinux.ezmybatis.mp.entity;

import org.rdlinux.ezmybatis.constant.DbType;

public class MpSqlServerEntityInfoBuilder extends MpMySqlEntityInfoBuilder {
    private static volatile MpSqlServerEntityInfoBuilder instance;

    protected MpSqlServerEntityInfoBuilder() {
    }

    public static MpSqlServerEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MpSqlServerEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MpSqlServerEntityInfoBuilder();
                }
            }
        }
        return instance;
    }


    @Override
    public DbType getSupportedDbType() {
        return DbType.SQL_SERVER;
    }
}
