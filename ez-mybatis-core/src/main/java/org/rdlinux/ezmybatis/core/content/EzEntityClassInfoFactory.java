package org.rdlinux.ezmybatis.core.content;

import java.util.HashMap;
import java.util.Map;

public class EzEntityClassInfoFactory {
    private static final Object lockO = new Object();
    private static Map<String, EntityClassInfo> infoMap = new HashMap<>();

    /**
     * @param ntClass                    实体对象类型
     * @param isMapUnderscoreToCamelCase 是否将下划线转换为驼峰
     */
    public static EntityClassInfo forClass(Class<?> ntClass, boolean isMapUnderscoreToCamelCase) {
        EntityClassInfo result = infoMap.get(ntClass.getName());
        if (result == null) {
            synchronized (lockO) {
                result = infoMap.get(ntClass.getName());
                if (result == null) {
                    result = buildInfo(ntClass, isMapUnderscoreToCamelCase);
                }
            }
        }
        return result;
    }

    private static EntityClassInfo buildInfo(Class<?> ntClass, boolean isMapUnderscoreToCamelCase) {
        EntityClassInfo entityClassInfo = new EntityClassInfo(ntClass, isMapUnderscoreToCamelCase);
        infoMap.put(ntClass.getName(), entityClassInfo);
        return entityClassInfo;
    }
}
