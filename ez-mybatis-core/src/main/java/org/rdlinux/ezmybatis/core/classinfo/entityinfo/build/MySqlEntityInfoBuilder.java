package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.DefaultEntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityInfoBuildConfig;

public class MySqlEntityInfoBuilder implements EntityInfoBuilder {
    private static volatile MySqlEntityInfoBuilder instance;

    private MySqlEntityInfoBuilder() {
    }

    public static MySqlEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (MySqlEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new MySqlEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig;
        //如果配置下划线转驼峰
        if (ezContentConfig.getEzMybatisConfig().getConfiguration().isMapUnderscoreToCamelCase()) {
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
