package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.DefaultEntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;

public class OracleEntityInfoBuild implements EntityInfoBuild {
    private static volatile OracleEntityInfoBuild instance;

    protected OracleEntityInfoBuild() {
    }

    public static OracleEntityInfoBuild getInstance() {
        if (instance == null) {
            synchronized (OracleEntityInfoBuild.class) {
                if (instance == null) {
                    instance = new OracleEntityInfoBuild();
                }
            }
        }
        return instance;
    }

    @Override
    public EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig;
        //如果配置下划线转驼峰
        if (ezContentConfig.getConfiguration().isMapUnderscoreToCamelCase()) {
            buildConfig = new EntityInfoBuildConfig(ezContentConfig.getEzMybatisConfig().getTableNamePattern(),
                    EntityInfoBuildConfig.ColumnHandle.TO_UNDER_AND_UPPER);
        } else {
            buildConfig = new EntityInfoBuildConfig(ezContentConfig.getEzMybatisConfig().getTableNamePattern(),
                    EntityInfoBuildConfig.ColumnHandle.ORIGINAL);
        }
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.ORACLE;
    }
}
