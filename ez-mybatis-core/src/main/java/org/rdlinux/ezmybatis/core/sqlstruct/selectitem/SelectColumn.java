package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
@Setter
public class SelectColumn extends AbstractSelectItem {
    protected Table table;
    protected String column;

    public SelectColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    public SelectColumn(Table table, String column, String alias) {
        this(table, column);
        this.setAlias(alias);
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + this.table.getAlias() + "." + this.column + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
