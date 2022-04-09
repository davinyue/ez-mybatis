package org.rdlinux.ezmybatis.core.content.entityinfo.build;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.content.entityinfo.DefaultEntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityInfoBuildConfig;
import org.rdlinux.ezmybatis.utils.HumpLineStringUtils;

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
    public EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass) {
        EntityInfoBuildConfig buildConfig;
        //如果配置下划线转驼峰
        if (configuration.isMapUnderscoreToCamelCase()) {
            buildConfig = new EntityInfoBuildConfig(EntityInfoBuildConfig.ColumnHandle.ToUnderAndUpper);
        } else {
            buildConfig = new EntityInfoBuildConfig(EntityInfoBuildConfig.ColumnHandle.ORIGINAL);
        }
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public String computeFieldNameByColumn(Configuration configuration, String column) {
        if (configuration.isMapUnderscoreToCamelCase()) {
            //大小写都有, 则直接返回
            if (column.matches("^.*[a-z]+.*$") && column.matches("^.*[A-Z]+.*$")) {
                return column;
            } else if (column.matches("^.*[a-z]+.*$")) {
                return HumpLineStringUtils.lineToHump(column, "_");
            } else {
                return HumpLineStringUtils.lineToHump(column.toLowerCase(), "_");
            }
        }
        return column.toLowerCase();
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.ORACLE;
    }
}
