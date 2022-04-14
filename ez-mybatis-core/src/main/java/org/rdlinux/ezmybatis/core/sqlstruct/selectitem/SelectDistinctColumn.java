package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectDistinctColumn extends SelectColumn {


    public SelectDistinctColumn(Table table, String column) {
        super(table, column);
    }

    public SelectDistinctColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " DISTINCT " + this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
