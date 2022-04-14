package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

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
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + this.syntax;
    }
}
