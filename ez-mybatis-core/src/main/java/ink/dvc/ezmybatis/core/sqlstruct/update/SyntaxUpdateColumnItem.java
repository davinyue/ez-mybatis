package ink.dvc.ezmybatis.core.sqlstruct.update;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;

public class SyntaxUpdateColumnItem extends UpdateItem {
    private String column;
    private String syntax;

    public SyntaxUpdateColumnItem(Table table, String column, String syntax) {
        super(table);
        this.column = column;
        this.syntax = syntax;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + this.syntax;
    }
}
