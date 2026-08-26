package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 动态物理表路由上下文。
 *
 * <p>上下文持有当前 SQL 生成过程使用的配置和物理表对象，并预先计算基础表名。
 * schema 与 partition 保留在物理表对象上，不在上下文中复制。</p>
 */
@Getter
public final class TableRouteContext {
    /**
     * MyBatis 配置。
     */
    private final Configuration configuration;
    /**
     * 当前物理表对象。
     */
    private final DbTable table;
    /**
     * 当前表实现解析出的基础表名。
     */
    private final String baseTableName;

    /**
     * 创建动态物理表路由上下文。
     *
     * @param configuration MyBatis 配置
     * @param table         当前物理表
     */
    public TableRouteContext(Configuration configuration, DbTable table) {
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(table, "table can not be null");
        this.configuration = configuration;
        this.table = table;
        this.baseTableName = table.getTableName(configuration);
    }

    /**
     * 获取实体类型。
     *
     * @return 实体类型；当前表不是 {@link EntityTable} 时返回 {@code null}
     */
    public Class<?> getEntityType() {
        if (this.table instanceof EntityTable) {
            return ((EntityTable) this.table).getEtType();
        }
        return null;
    }

}
