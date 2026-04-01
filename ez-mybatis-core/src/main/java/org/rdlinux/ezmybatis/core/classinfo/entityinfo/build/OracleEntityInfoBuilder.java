package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

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
                .getTableNameCasePolicy(), EntityInfoBuildConfig.ColumnNameBuildPolicy.TO_UNDER_AND_UPPER,
                ezContentConfig.getEzMybatisConfig().getColumnNameCasePolicy());
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }

}
