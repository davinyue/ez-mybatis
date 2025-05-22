package org.rdlinux.ezmybatis.mp.entity;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;

public class MpOracleEntityInfoBuilder implements EntityInfoBuilder {
    private static volatile MpOracleEntityInfoBuilder instance;

    protected MpOracleEntityInfoBuilder() {
    }

    public static MpOracleEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MpOracleEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MpOracleEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig = new EntityInfoBuildConfig(ezContentConfig
                .getEzMybatisConfig().getTableNamePattern(), EntityInfoBuildConfig.ColumnHandle.TO_UNDER_AND_UPPER);
        return new MpEntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.ORACLE;
    }
}
