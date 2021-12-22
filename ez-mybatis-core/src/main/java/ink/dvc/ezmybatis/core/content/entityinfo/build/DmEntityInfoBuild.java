package ink.dvc.ezmybatis.core.content.entityinfo.build;

import ink.dvc.ezmybatis.core.constant.DbType;

public class DmEntityInfoBuild extends OracleEntityInfoBuild {
    private static volatile DmEntityInfoBuild instance;

    private DmEntityInfoBuild() {
    }

    public static DmEntityInfoBuild getInstance() {
        if (instance == null) {
            synchronized (DmEntityInfoBuild.class) {
                if (instance == null) {
                    instance = new DmEntityInfoBuild();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportedDbType() {
        return DbType.DM;
    }
}
