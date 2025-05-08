package org.rdlinux.ezmybatis.mp.entity;

import org.rdlinux.ezmybatis.constant.DbType;

public class MpDmEntityInfoBuilder extends MpOracleEntityInfoBuilder {
    private static volatile MpDmEntityInfoBuilder instance;

    private MpDmEntityInfoBuilder() {
    }

    public static MpDmEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MpDmEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MpDmEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.DM;
    }
}
