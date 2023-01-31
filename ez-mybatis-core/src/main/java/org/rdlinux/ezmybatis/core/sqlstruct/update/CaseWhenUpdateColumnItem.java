package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

@Getter
public class CaseWhenUpdateColumnItem extends UpdateItem {
    private String column;
    private CaseWhen caseWhen;

    public CaseWhenUpdateColumnItem(Table table, String column, CaseWhen caseWhen) {
        super(table);
        this.column = column;
        this.caseWhen = caseWhen;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        StringBuilder caseWhenSql = converter.toSqlPart(Converter.Type.UPDATE, new StringBuilder(), configuration,
                this.caseWhen, mybatisParamHolder);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + caseWhenSql.toString();
    }
}
