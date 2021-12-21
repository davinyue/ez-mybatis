package ink.dvc.ezmybatis.core.content.entityinfo.build;

import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityInfoBuildConfig;
import ink.dvc.ezmybatis.core.utils.HumpLineStringUtils;
import org.apache.ibatis.session.Configuration;

public class OracleEntityInfoBuild implements EntityInfoBuild {
    private static volatile OracleEntityInfoBuild instance;

    private OracleEntityInfoBuild() {
    }

    public static OracleEntityInfoBuild getInstance() {
        if (instance == null) {
            synchronized ( OracleEntityInfoBuild.class ) {
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
        return new EntityClassInfo(ntClass, buildConfig);
    }

    @Override
    public String computeFieldNameByColumn(Configuration configuration, String column) {
        if (configuration.isMapUnderscoreToCamelCase()) {
            return HumpLineStringUtils.lineToHump(column.toLowerCase(), "_");
        }
        return column.toLowerCase();
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.ORACLE;
    }
}
