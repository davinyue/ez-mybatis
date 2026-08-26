package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 表结构 SQL 渲染工具。
 *
 * <p>工具使用独立的 SQL 构建器渲染表结构，避免在主 SQL 上下文中临时拼接后再清空，
 * 同时确保所有物理表都经过方言转换器和动态路由处理。</p>
 */
public final class TableSqlRenderer {

    private TableSqlRenderer() {
    }

    /**
     * 使用当前配置的表转换器渲染表结构。
     *
     * @param sqlGenerateContext SQL 生成上下文
     * @param table              待渲染表
     * @param type               表结构使用的 SQL 类型
     * @return 表结构 SQL
     */
    public static String render(SqlGenerateContext sqlGenerateContext, Table table, Converter.Type type) {
        Assert.notNull(sqlGenerateContext, "sqlGenerateContext can not be null");
        Assert.notNull(table, "table can not be null");
        Assert.notNull(type, "type can not be null");
        StringBuilder sqlBuilder = new StringBuilder();
        SqlGenerateContext tableContext = SqlGenerateContext.copyOf(sqlGenerateContext, sqlBuilder);
        Converter<?> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), table.getClass());
        converter.buildSql(type, table, tableContext);
        return sqlBuilder.toString();
    }
}
