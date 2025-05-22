package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.DefaultEntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;

public class OracleEntityInfoBuilder implements EntityInfoBuilder {
    private static volatile OracleEntityInfoBuilder instance;

    protected OracleEntityInfoBuilder() {
    }

    public static OracleEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (OracleEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new OracleEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig = new EntityInfoBuildConfig(ezContentConfig.getEzMybatisConfig()
                .getTableNamePattern(), EntityInfoBuildConfig.ColumnHandle.TO_UNDER_AND_UPPER);
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.ORACLE;
    }
}
