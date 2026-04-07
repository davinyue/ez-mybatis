package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzEntityClassInfoFactory {
    /**
     * 锁映射
     */
    private static final ConcurrentMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();
    /**
     * 默认实体信息缓存
     */
    private static EzMybatisEntityInfoCache ENTITY_INFO_CACHE = new DefaultEzMybatisEntityInfoCache();
    /**
     * 默认实体信息热加载通知器
     */
    private static EntityInfoReloadNotifier ENTITY_INFO_RELOAD_NOTIFIER =
            new DefaultEntityInfoReloadNotifier(ENTITY_INFO_CACHE);

    static {
        new EntityInfoHotReloadManager(new BuildOutputPathResolver(),
                EzEntityClassInfoFactory::getEntityInfoReloadNotifier).start();
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
                    EntityInfoBuilder infoBuilder = EzMybatisContent.getDbDialectProvider(configuration).
                            getEntityInfoBuilder();
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
     * 设置实体信息缓存
     */
    public static void setEntityInfoCache(EzMybatisEntityInfoCache entityInfoCache) {
        Assert.notNull(entityInfoCache, "The entity information cache cannot be null.");
        EzEntityClassInfoFactory.ENTITY_INFO_CACHE = entityInfoCache;
        EzEntityClassInfoFactory.ENTITY_INFO_RELOAD_NOTIFIER = new DefaultEntityInfoReloadNotifier(entityInfoCache);
    }

    /**
     * 获取实体信息热加载通知器
     */
    public static EntityInfoReloadNotifier getEntityInfoReloadNotifier() {
        return ENTITY_INFO_RELOAD_NOTIFIER;
    }

    /**
     * 设置实体信息热加载通知器
     */
    public static void setEntityInfoReloadNotifier(EntityInfoReloadNotifier entityInfoReloadNotifier) {
        Assert.notNull(entityInfoReloadNotifier, "The entity information reload notifier cannot be null.");
        EzEntityClassInfoFactory.ENTITY_INFO_RELOAD_NOTIFIER = entityInfoReloadNotifier;
    }

    /**
     * 清理指定 configuration 下的实体信息缓存
     */
    public static void clear(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can not be null");
        ENTITY_INFO_CACHE.clear(configuration);
    }

    /**
     * 清理全部实体信息缓存
     */
    public static void clear() {
        ENTITY_INFO_CACHE.clear();
    }
}
