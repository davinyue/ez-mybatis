package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultEzMybatisEntityInfoCache implements EzMybatisEntityInfoCache {
    /**
     * 实体信息映射
     */
    protected static final ConcurrentMap<Configuration, ConcurrentMap<String, EntityClassInfo>> ENTITY_INFO_MAP = new ConcurrentHashMap<>();
    private static final Log log = LogFactory.getLog(DefaultEzMybatisEntityInfoCache.class);

    @Override
    public EntityClassInfo get(Configuration configuration, Class<?> ntClass) {
        Assert.notNull(configuration, "Configuration can not be null");
        Assert.notNull(ntClass, "NtClass can not be null");
        Map<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.get(configuration);
        if (entityInfo == null) {
            return null;
        }
        return entityInfo.get(ntClass.getName());
    }

    @Override
    public void set(Configuration configuration, EntityClassInfo entityClassInfo) {
        Assert.notNull(configuration, "Configuration can not be null");
        Assert.notNull(entityClassInfo, "EntityClassInfo can not be null");
        ConcurrentMap<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.computeIfAbsent(configuration,
                k -> new ConcurrentHashMap<>());
        entityInfo.put(entityClassInfo.getEntityClass().getName(), entityClassInfo);
    }

    @Override
    public void remove(Class<?> ntClass) {
        Assert.notNull(ntClass, "NtClass can not be null");
        this.remove(ntClass.getName());
    }

    @Override
    public void remove(String className) {
        Assert.notNull(className, "ClassName can not be null");
        boolean cleaned = false;
        for (ConcurrentMap<String, EntityClassInfo> cache : ENTITY_INFO_MAP.values()) {
            EntityClassInfo classInfo = cache.remove(className);
            if (classInfo != null) {
                cleaned = true;
            }
        }
        if (cleaned && log.isDebugEnabled()) {
            log.debug(String.format("Cleaning the class information of %s", className));
        }
    }

    @Override
    public void clear(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can not be null");
        Map<String, EntityClassInfo> removed = ENTITY_INFO_MAP.remove(configuration);
        if (removed != null) {
            removed.clear();
            if (log.isDebugEnabled()) {
                log.debug("Cleaning class information caches for the specified configuration.");
            }
        }
    }

    @Override
    public void clear() {
        for (Map<String, EntityClassInfo> cache : ENTITY_INFO_MAP.values()) {
            cache.clear();
        }
        ENTITY_INFO_MAP.clear();
        if (log.isDebugEnabled()) {
            log.debug("Cleaning all class information caches.");
        }
    }
}
