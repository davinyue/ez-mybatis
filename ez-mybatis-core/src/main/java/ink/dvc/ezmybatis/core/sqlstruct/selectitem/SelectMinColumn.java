package ink.dvc.ezmybatis.core.sqlstruct.selectitem;

import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;

public class SelectMinColumn extends SelectColumn {


    public SelectMinColumn(Table table, String column) {
        super(table, column);
    }

    public SelectMinColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        String sql = " MIN(" + this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM + ") ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
