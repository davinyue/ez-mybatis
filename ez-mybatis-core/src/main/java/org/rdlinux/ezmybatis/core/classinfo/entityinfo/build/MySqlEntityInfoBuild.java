package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
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
    public EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig;
        //如果配置下划线转驼峰
        if (configuration.isMapUnderscoreToCamelCase()) {
            buildConfig = new EntityInfoBuildConfig(EntityInfoBuildConfig.ColumnHandle.ToUnder);
        } else {
            buildConfig = new EntityInfoBuildConfig(EntityInfoBuildConfig.ColumnHandle.ORIGINAL);
        }
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }


    @Override
    public DbType getSupportedDbType() {
        return DbType.MYSQL;
    }
}
