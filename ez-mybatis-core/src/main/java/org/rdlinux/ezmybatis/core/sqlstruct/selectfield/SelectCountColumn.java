package org.rdlinux.ezmybatis.core.sqlstruct.selectfield;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

public class SelectCountColumn extends SelectColumn {


    public SelectCountColumn(Table table, String column) {
        super(table, column);
    }

    public SelectCountColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        String sql = " COUNT(" + this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM + ") ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
