package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.DmEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.MySqlEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.OracleEntityInfoBuilder;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzEntityClassInfoFactory {
    /**
     * 实体信息构建工厂
     */
    private static final Map<DbType, EntityInfoBuilder> ENTITY_INFO_BUILD_MAP = new ConcurrentHashMap<>();
    /**
     * 默认实体信息缓存
     */
    private static EzMybatisEntityInfoCache ENTITY_INFO_CACHE = new DefaultEzMybatisEntityInfoCache();
    /**
     * 锁映射
     */
    private static final ConcurrentMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();

    //初始化实体构建信息
    static {
        MySqlEntityInfoBuilder mySqlEntityInfoBuild = MySqlEntityInfoBuilder.getInstance();
        ENTITY_INFO_BUILD_MAP.put(mySqlEntityInfoBuild.getSupportedDbType(), mySqlEntityInfoBuild);
        OracleEntityInfoBuilder oracleEntityInfoBuild = OracleEntityInfoBuilder.getInstance();
        ENTITY_INFO_BUILD_MAP.put(oracleEntityInfoBuild.getSupportedDbType(), oracleEntityInfoBuild);
        DmEntityInfoBuilder dmEntityInfoBuild = DmEntityInfoBuilder.getInstance();
        ENTITY_INFO_BUILD_MAP.put(dmEntityInfoBuild.getSupportedDbType(), dmEntityInfoBuild);
        ENTITY_INFO_BUILD_MAP.put(DbType.POSTGRE_SQL, mySqlEntityInfoBuild);
        ENTITY_INFO_BUILD_MAP.put(DbType.SQL_SERVER, mySqlEntityInfoBuild);
    }

    /**
     * 获取实体信息, 如果没有将构造实体信息返回
     *
     * @param ntClass       实体对象类型
     * @param configuration mybatis配置
     */
    public static EntityClassInfo forClass(Configuration configuration, Class<?> ntClass) {
        EntityClassInfo result = ENTITY_INFO_CACHE.get(configuration, ntClass);
        if (result == null) {
            String lockKey = configuration.hashCode() + "." + ntClass.hashCode();
            Object lockObj = LOCK_MAP.computeIfAbsent(lockKey, (k) -> new Object());
            synchronized (lockObj) {
                result = ENTITY_INFO_CACHE.get(configuration, ntClass);
                if (result == null) {
                    DbType dbType = EzMybatisContent.getDbType(configuration);
                    EntityInfoBuilder infoBuilder = ENTITY_INFO_BUILD_MAP.get(dbType);
                    EzContentConfig ezContentConfig = EzMybatisContent.getContentConfig(configuration);
                    result = infoBuilder.buildInfo(ezContentConfig, ntClass);
                    ENTITY_INFO_CACHE.set(configuration, result);
                }
            }
            LOCK_MAP.remove(lockKey);
        }
        return result;
    }

    /**
     * 设置实体信息构造器
     */
    public static void setEntityInfoBuilder(EntityInfoBuilder entityInfoBuilder) {
        Assert.notNull(entityInfoBuilder, "The entity information builder cannot be empty.");
        Assert.notNull(entityInfoBuilder.getSupportedDbType(),
                "The supported database type cannot be empty.");
        ENTITY_INFO_BUILD_MAP.put(entityInfoBuilder.getSupportedDbType(), entityInfoBuilder);
    }

    /**
     * 根据配置获取实体信息构造器
     */
    public static EntityInfoBuilder getEntityInfoBuilder(Configuration configuration) {
        return ENTITY_INFO_BUILD_MAP.get(EzMybatisContent.getDbType(configuration));
    }

    /**
     * 设置实体信息缓存
     */
    public static void setEntityInfoCache(EzMybatisEntityInfoCache entityInfoCache) {
        Assert.notNull(entityInfoCache, "The entity information cache cannot be null.");
        EzEntityClassInfoFactory.ENTITY_INFO_CACHE = entityInfoCache;
    }
}
