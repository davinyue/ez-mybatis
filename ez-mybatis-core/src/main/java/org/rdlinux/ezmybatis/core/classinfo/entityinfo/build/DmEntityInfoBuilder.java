package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;

public class DmEntityInfoBuilder extends OracleEntityInfoBuilder {
    private static volatile DmEntityInfoBuilder instance;

    private DmEntityInfoBuilder() {
    }

    public static DmEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (DmEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new DmEntityInfoBuilder();
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
