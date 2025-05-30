package org.rdlinux.ezmybatis.mp.entity;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;

public class MpMySqlEntityInfoBuilder implements EntityInfoBuilder {
    private static volatile MpMySqlEntityInfoBuilder instance;

    protected MpMySqlEntityInfoBuilder() {
    }

    public static MpMySqlEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MpMySqlEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MpMySqlEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig = new EntityInfoBuildConfig(ezContentConfig
                .getEzMybatisConfig().getTableNamePattern(), EntityInfoBuildConfig.ColumnHandle.TO_UNDER);
        return new MpEntityClassInfo(ntClass, buildConfig);
    }


    @Override
    public DbType getSupportedDbType() {
        return DbType.MYSQL;
    }
}
