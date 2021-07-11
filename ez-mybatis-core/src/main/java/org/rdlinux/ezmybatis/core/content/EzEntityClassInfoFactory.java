package org.rdlinux.ezmybatis.core.content;

import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

public class EzEntityClassInfoFactory {
    private static final Object lockO = new Object();
    private static Map<String, EntityClassInfo> infoMap = new HashMap<>();

    /**
     * @param ntClass       实体对象类型
     * @param configuration mybatis配置
     */
    public static EntityClassInfo forClass(Configuration configuration, Class<?> ntClass) {
        EntityClassInfo result = infoMap.get(ntClass.getName());
        if (result == null) {
            synchronized (lockO) {
                result = infoMap.get(ntClass.getName());
                if (result == null) {
                    result = buildInfo(configuration, ntClass);
                }
            }
        }
        return result;
    }

    private static EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass) {
        EntityClassInfo entityClassInfo = new EntityClassInfo(ntClass, configuration.isMapUnderscoreToCamelCase());
        infoMap.put(ntClass.getName(), entityClassInfo);
        return entityClassInfo;
    }
}
