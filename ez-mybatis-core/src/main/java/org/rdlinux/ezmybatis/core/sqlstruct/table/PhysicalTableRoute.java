package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

/**
 * 动态物理表路由结果。
 *
 * <p>当路由器返回该对象时，三个字段整体表示本次 SQL 使用的物理表信息：
 * schema 和 partition 可以为 {@code null}，分别表示不使用 schema 和 partition；tableName 必须有值。
 * partition 使用结构对象表示，不允许直接携带 SQL 片段。</p>
 */
@Getter
public final class PhysicalTableRoute {
    /**
     * 动态 schema。
     */
    private final String schema;
    /**
     * 动态表名。
     */
    private final String tableName;
    /**
     * 动态分区。
     */
    private final Partition partition;

    /**
     * 创建物理表路由结果。
     *
     * @param schema    动态 schema；可以为 {@code null}
     * @param tableName 动态表名；不能为空
     * @param partition 动态分区；可以为 {@code null}
     */
    public PhysicalTableRoute(String schema, String tableName, Partition partition) {
        this.schema = schema;
        this.tableName = tableName;
        this.partition = partition;
    }

    /**
     * 创建物理表路由结果。
     *
     * @param schema    动态 schema；可以为 {@code null}
     * @param tableName 动态表名；不能为空
     * @param partition 动态分区；可以为 {@code null}
     * @return 物理表路由结果
     */
    public static PhysicalTableRoute of(String schema, String tableName, Partition partition) {
        return new PhysicalTableRoute(schema, tableName, partition);
    }
}
