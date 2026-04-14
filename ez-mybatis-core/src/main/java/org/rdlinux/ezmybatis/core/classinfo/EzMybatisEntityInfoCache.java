package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;

public interface EzMybatisEntityInfoCache {
    /**
     * 获取实体信息缓存
     */
    EntityClassInfo get(Configuration configuration, Class<?> ntClass);

    /**
     * 设置实体信息缓存, 该方法由EzEntityClassInfoFactory在构造实体信息时保存实体信息调用,
     * 该方法需要线程安全
     */
    void set(Configuration configuration, EntityClassInfo entityClassInfo);

    /**
     * 移除指定实体类型对应的缓存
     */
    void remove(Class<?> ntClass);

    /**
     * 按类名移除缓存
     */
    void remove(String className);

    /**
     * 清空指定 Configuration 下的缓存
     */
    void clear(Configuration configuration);

    /**
     * 清空全部缓存
     */
    void clear();
}
