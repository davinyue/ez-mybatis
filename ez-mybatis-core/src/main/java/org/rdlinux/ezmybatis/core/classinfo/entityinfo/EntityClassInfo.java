package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import java.util.List;
import java.util.Map;

public interface EntityClassInfo {
    /**
     * 获取实体类
     */
    Class<?> getEntityClass();

    /**
     * 获取数据库模式
     */
    String getSchema();

    /**
     * 获取表明
     */
    String getTableName();

    /**
     * 获取属性信息
     */
    List<EntityFieldInfo> getFieldInfos();

    /**
     * 获取列与属性信息映射
     */
    Map<String, EntityFieldInfo> getColumnMapFieldInfo();

    /**
     * 根据列名获取属性名
     */
    String getFieldNameByColumn(String column);

    /**
     * 根据属性名获取属性信息
     */
    EntityFieldInfo getFieldInfo(String field);

    /**
     * 获取主键信息
     */
    EntityFieldInfo getPrimaryKeyInfo();

}
