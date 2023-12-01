package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;

public interface EzMybatisEntityInfoCache {
    /**
     * 获取实体信息缓存
     */
    EntityClassInfo get(Configuration configuration, Class<?> ntClass);

    /**
     * 设置实体信息缓存
     */
    void set(Configuration configuration, EntityClassInfo entityClassInfo);
}
