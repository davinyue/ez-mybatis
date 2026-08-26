package org.rdlinux.ezmybatis.core.sqlstruct.table;

/**
 * 动态物理表路由器。
 *
 * <p>每个 MyBatis {@link org.apache.ibatis.session.Configuration} 最多注册一个路由器。
 * 路由器负责根据当前物理表的基础信息生成本次 SQL 使用的 schema、表名和分区。</p>
 */
@FunctionalInterface
public interface DynamicTableResolver {

    /**
     * 解析当前物理表的动态路由结果。
     *
     * @param context 当前物理表路由上下文
     * @return 动态路由结果；返回 {@code null} 表示沿用基础表结构
     */
    PhysicalTableRoute resolve(TableRouteContext context);
}
