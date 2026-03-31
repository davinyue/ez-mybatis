package org.rdlinux.ezmybatis.core;

/**
 * 实体属性访问域
 */
public enum FieldAccessScope {
    /**
     * 整体实体持久化场景：insert / update / replace / batchInsert 等，
     * 实体全部（或非null）字段被遍历写入数据库。
     * <p>典型用途包括：将 where 条件中的明文值加密后与数据库密文进行比对，
     * 以及 EzUpdate 中对指定字段设置值时的加密处理。</p>
     */
    ENTITY_PERSIST,
    /**
     * DSL 条件构造场景：EzQuery/EzUpdate/EzDelete 的 where/set 中，
     * 用户手动绑定的某个字段值。
     * 典型用途：将 where 条件中的明文值加密后进行密文比对。
     */
    DSL_CONDITION
}
