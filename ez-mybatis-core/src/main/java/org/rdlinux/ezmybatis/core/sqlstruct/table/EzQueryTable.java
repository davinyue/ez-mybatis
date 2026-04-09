package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 子查询表结构。
 *
 * <p>将 {@link EzQuery} 包装为可参与 FROM/JOIN 的派生表，并自动生成别名。</p>
 */
@Setter
@Getter
public class EzQueryTable extends AbstractTable implements Table {
    /**
     * 作为派生表来源的查询对象。
     */
    private EzQuery<?> ezQuery;

    /**
     * 使用子查询初始化派生表对象。
     *
     * @param ezQuery 子查询对象
     */
    private EzQueryTable(EzQuery<?> ezQuery) {
        super(AliasGenerate.getAlias(), null);
        Assert.notNull(ezQuery, "ezQuery can not be null");
        this.ezQuery = ezQuery;
    }

    /**
     * 通过查询对象创建派生表。
     *
     * @param ezQuery 子查询对象
     * @return 子查询表对象
     */
    public static EzQueryTable of(EzQuery<?> ezQuery) {
        return new EzQueryTable(ezQuery);
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
     * 派生表在外层 SQL 中以别名参与引用。
     *
     * @param configuration MyBatis 配置
     * @return 派生表别名
     */
    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    /**
     * 子查询表不直接绑定 schema。
     *
     * @param configuration MyBatis 配置
     * @return 固定返回 {@code null}
     */
    @Override
    public String getSchema(Configuration configuration) {
        return null;
    }
}
