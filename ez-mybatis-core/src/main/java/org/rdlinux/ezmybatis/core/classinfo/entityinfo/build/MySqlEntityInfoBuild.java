package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.DefaultEntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;

public class MySqlEntityInfoBuild implements EntityInfoBuild {
    private static volatile MySqlEntityInfoBuild instance;

    private MySqlEntityInfoBuild() {
    }

    public static MySqlEntityInfoBuild getInstance() {
        if (instance == null) {
            synchronized (MySqlEntityInfoBuild.class) {
                if (instance == null) {
                    instance = new MySqlEntityInfoBuild();
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
                    EntityInfoBuildConfig.ColumnHandle.TO_UNDER);
        } else {
            buildConfig = new EntityInfoBuildConfig(ezContentConfig.getEzMybatisConfig().getTableNamePattern(),
                    EntityInfoBuildConfig.ColumnHandle.ORIGINAL);
        }
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }


    @Override
    public DbType getSupportedDbType() {
        return DbType.MYSQL;
    }
}
