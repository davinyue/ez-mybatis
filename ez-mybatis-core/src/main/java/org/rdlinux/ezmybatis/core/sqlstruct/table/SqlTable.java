package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 原生 SQL 派生表结构。
 *
 * <p>用于将一段自定义 SQL 作为表对象参与查询，并自动生成别名。</p>
 */
@Setter
@Getter
public class SqlTable extends AbstractTable {
    /**
     * 作为派生表来源的 SQL 片段。
     */
    private String sql;

    /**
     * 使用原生 SQL 初始化派生表对象。
     *
     * @param sql 原生 SQL
     */
    private SqlTable(String sql) {
        super(AliasGenerate.getAlias());
        Assert.notEmpty(sql, "sql can not be null");
        this.sql = sql;
    }

    /**
     * 通过原生 SQL 创建派生表对象。
     *
     * @param sql 原生 SQL
     * @return SQL 表对象
     */
    public static SqlTable of(String sql) {
        return new SqlTable(sql);
    }

    /**
     * 返回派生表别名。
     *
     * @return 表别名
     */
    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * 原生 SQL 派生表在外层 SQL 中以别名参与引用。
     *
     * @param configuration MyBatis 配置
     * @return 派生表别名
     */
    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    /**
     * 原生 SQL 派生表不直接绑定 schema。
     *
     * @param configuration MyBatis 配置
     * @return 固定返回 {@code null}
     */
    @Override
    public String getSchema(Configuration configuration) {
        return null;
    }
}
