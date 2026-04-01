package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

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
        EntityInfoBuildConfig buildConfig = new EntityInfoBuildConfig(ezContentConfig.getEzMybatisConfig()
                .getTableNameCasePolicy(), EntityInfoBuildConfig.ColumnNameBuildPolicy.TO_UNDER,
                ezContentConfig.getEzMybatisConfig().getColumnNameCasePolicy());
        return new DefaultEntityClassInfo(ntClass, buildConfig);
    }


}
