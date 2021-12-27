package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import org.apache.ibatis.session.Configuration;

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
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + this.syntax;
    }
}
