package org.rdlinux.ezmybatis.core.classinfo;

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
    private static final ConcurrentMap<Configuration, ConcurrentMap<String, EntityClassInfo>> ENTITY_INFO_MAP =
            new ConcurrentHashMap<>();

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
        ConcurrentMap<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.get(configuration);
        if (entityInfo == null) {
            entityInfo = new ConcurrentHashMap<>();
            ENTITY_INFO_MAP.put(configuration, entityInfo);
        }
        entityInfo.put(entityClassInfo.getEntityClass().getName(), entityClassInfo);
    }
}
