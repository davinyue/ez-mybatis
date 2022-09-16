package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.DmEntityInfoBuild;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuild;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.MySqlEntityInfoBuild;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.OracleEntityInfoBuild;

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

    private static EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass,
                                             EntityInfoBuild entityInfoBuild) {
        DbType dbType = EzMybatisContent.getDbType(configuration);
        EntityInfoBuild infoBuild = ENTITY_INFO_BUILD_MAP.get(dbType);
        if (entityInfoBuild != null) {
            infoBuild = entityInfoBuild;
        }
        EntityClassInfo entityClassInfo = infoBuild.buildInfo(configuration, ntClass);
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
        return forClass(configuration, ntClass, null);
    }

    /**
     * @param ntClass         实体对象类型
     * @param configuration   mybatis配置
     * @param entityInfoBuild 实体信息构造器
     */
    public static EntityClassInfo forClass(Configuration configuration, Class<?> ntClass,
                                           EntityInfoBuild entityInfoBuild) {
        EntityClassInfo result = get(configuration, ntClass);
        if (result == null) {
            synchronized (configuration) {
                result = get(configuration, ntClass);
                if (result == null) {
                    result = buildInfo(configuration, ntClass, entityInfoBuild);
                }
            }
        }
        return result;
    }

    public static EntityInfoBuild getEntityInfoBuild(Configuration configuration) {
        return ENTITY_INFO_BUILD_MAP.get(EzMybatisContent.getDbType(configuration));
    }
}
