package org.rdlinux.ezmybatis.core.content.entityinfo.build;

import org.rdlinux.ezmybatis.core.constant.DbType;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityInfoBuildConfig;
import org.rdlinux.ezmybatis.core.utils.HumpLineStringUtils;
import org.apache.ibatis.session.Configuration;

public class MySqlEntityInfoBuild implements EntityInfoBuild {
    private static volatile MySqlEntityInfoBuild instance;

    private MySqlEntityInfoBuild() {
    }

    public static MySqlEntityInfoBuild getInstance() {
        if (instance == null) {
            synchronized ( MySqlEntityInfoBuild.class ) {
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
        return new EntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public String computeFieldNameByColumn(Configuration configuration, String column) {
        if (configuration.isMapUnderscoreToCamelCase()) {
            return HumpLineStringUtils.lineToHump(column, "_");
        }
        return column.toLowerCase();
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.MYSQL;
    }
}
