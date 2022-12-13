package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectCountColumn extends SelectColumn {
    /**
     * 是否去重
     */
    private boolean distinct = false;

    public SelectCountColumn(Table table, String column) {
        super(table, column);
    }

    public SelectCountColumn(Table table, boolean distinct, String column) {
        super(table, column);
        this.distinct = distinct;
    }

    public SelectCountColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }

    public SelectCountColumn(Table table, boolean distinct, String column, String alias) {
        super(table, column, alias);
        this.distinct = distinct;
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String distinctStr = "";
        if (this.distinct) {
            distinctStr = " DISTINCT ";
        }
        String sql = " COUNT(" + distinctStr + this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM + ") ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
