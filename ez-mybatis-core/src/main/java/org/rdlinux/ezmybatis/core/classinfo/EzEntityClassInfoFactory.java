package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实体元数据入口工厂。
 *
 * <p>该类负责按 {@link Configuration} 与实体类型构建并缓存 {@link EntityClassInfo}，
 * 同时协调热加载通知器与缓存失效逻辑。虽然命名为 Factory，但其职责还包括缓存复用、
 * 并发构建保护以及热加载期间的刷新协作。</p>
 */
public class EzEntityClassInfoFactory {
    /**
     * 按配置与实体类型组合生成的构建锁映射，用于避免并发重复构建。
     */
    private static final ConcurrentMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();
    /**
     * 当前生效的实体信息缓存实现。
     */
    private static EzMybatisEntityInfoCache ENTITY_INFO_CACHE = new DefaultEzMybatisEntityInfoCache();
    /**
     * 当前生效的实体信息热加载通知器。
     */
    private static EntityInfoReloadNotifier ENTITY_INFO_RELOAD_NOTIFIER =
            new DefaultEntityInfoReloadNotifier(ENTITY_INFO_CACHE);

    /**
     * 启动默认的实体信息热加载管理器。
     */
    static {
        new EntityInfoHotReloadManager(new BuildOutputPathResolver(),
                EzEntityClassInfoFactory::getEntityInfoReloadNotifier).start();
    }

    /**
     * 获取指定实体类型的元数据信息，不存在时按当前方言的实体信息构造器进行构建。
     *
     * @param configuration mybatis配置
     * @param ntClass       实体对象类型
     * @return 实体元数据
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
     * 设置实体信息缓存实现。
     *
     * <p>更新缓存后会同步重建默认的热加载通知器，使后续热加载失效动作与新缓存实现保持一致。</p>
     *
     * @param entityInfoCache 实体信息缓存实现
     */
    public static void setEntityInfoCache(EzMybatisEntityInfoCache entityInfoCache) {
        Assert.notNull(entityInfoCache, "The entity information cache cannot be null.");
        EzEntityClassInfoFactory.ENTITY_INFO_CACHE = entityInfoCache;
        EzEntityClassInfoFactory.ENTITY_INFO_RELOAD_NOTIFIER = new DefaultEntityInfoReloadNotifier(entityInfoCache);
    }

    /**
     * 获取当前使用的实体信息热加载通知器。
     *
     * @return 实体信息热加载通知器
     */
    public static EntityInfoReloadNotifier getEntityInfoReloadNotifier() {
        return ENTITY_INFO_RELOAD_NOTIFIER;
    }

    /**
     * 设置实体信息热加载通知器。
     *
     * @param entityInfoReloadNotifier 热加载通知器
     */
    public static void setEntityInfoReloadNotifier(EntityInfoReloadNotifier entityInfoReloadNotifier) {
        Assert.notNull(entityInfoReloadNotifier, "The entity information reload notifier cannot be null.");
        EzEntityClassInfoFactory.ENTITY_INFO_RELOAD_NOTIFIER = entityInfoReloadNotifier;
    }

    /**
     * 清理指定 {@link Configuration} 下的实体信息缓存。
     *
     * @param configuration MyBatis 配置对象
     */
    public static void clear(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can not be null");
        ENTITY_INFO_CACHE.clear(configuration);
    }

    /**
     * 清理全部实体信息缓存。
     */
    public static void clear() {
        ENTITY_INFO_CACHE.clear();
    }
}
