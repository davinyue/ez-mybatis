package org.rdlinux.ezmybatis.core.content;

import org.rdlinux.ezmybatis.core.constant.DbType;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.build.DmEntityInfoBuild;
import org.rdlinux.ezmybatis.core.content.entityinfo.build.EntityInfoBuild;
import org.rdlinux.ezmybatis.core.content.entityinfo.build.MySqlEntityInfoBuild;
import org.rdlinux.ezmybatis.core.content.entityinfo.build.OracleEntityInfoBuild;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

public class EzEntityClassInfoFactory {
    /**
     * 实体信息映射
     */
    private static final Map<Configuration, Map<String, EntityClassInfo>> ENTITY_INFO_MAP = new HashMap<>();
    /**
     * 实体信息构建工厂
     */
    private static final Map<DbType, EntityInfoBuild> ENTITY_INFO_BUILD_MAP = new HashMap<>();

    //初始化实体构建信息
    static {
        MySqlEntityInfoBuild mySqlEntityInfoBuild = MySqlEntityInfoBuild.getInstance();
        ENTITY_INFO_BUILD_MAP.put(mySqlEntityInfoBuild.getSupportedDbType(), mySqlEntityInfoBuild);
        OracleEntityInfoBuild oracleEntityInfoBuild = OracleEntityInfoBuild.getInstance();
        ENTITY_INFO_BUILD_MAP.put(oracleEntityInfoBuild.getSupportedDbType(), oracleEntityInfoBuild);
        DmEntityInfoBuild dmEntityInfoBuild = DmEntityInfoBuild.getInstance();
        ENTITY_INFO_BUILD_MAP.put(dmEntityInfoBuild.getSupportedDbType(), dmEntityInfoBuild);
    }

    private static EntityClassInfo get(Configuration configuration, Class<?> ntClass) {
        Map<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.get(configuration);
        if (entityInfo == null) {
            return null;
        }
        return entityInfo.get(ntClass.getName());
    }

    private static EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass) {
        DbType dbType = DbTypeUtils.getDbType(configuration);
        EntityClassInfo entityClassInfo = ENTITY_INFO_BUILD_MAP.get(dbType).buildInfo(configuration, ntClass);
        Map<String, EntityClassInfo> entityInfo;
        if (ENTITY_INFO_MAP.get(configuration) == null) {
            entityInfo = new HashMap<>();
            ENTITY_INFO_MAP.put(configuration, entityInfo);
        } else {
            entityInfo = ENTITY_INFO_MAP.get(configuration);
        }
        entityInfo.put(ntClass.getName(), entityClassInfo);
        return entityClassInfo;
    }

    /**
     * @param ntClass       实体对象类型
     * @param configuration mybatis配置
     */
    public static EntityClassInfo forClass(Configuration configuration, Class<?> ntClass) {
        EntityClassInfo result = get(configuration, ntClass);
        if (result == null) {
            synchronized (configuration) {
                result = get(configuration, ntClass);
                if (result == null) {
                    result = buildInfo(configuration, ntClass);
                }
            }
        }
        return result;
    }

    public static EntityInfoBuild getEntityInfoBuild(Configuration configuration) {
        return ENTITY_INFO_BUILD_MAP.get(DbTypeUtils.getDbType(configuration));
    }
}
