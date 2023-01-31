package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class CaseWhenUpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private CaseWhen caseWhen;

    public CaseWhenUpdateFieldItem(EntityTable table, String field, CaseWhen caseWhen) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.caseWhen = caseWhen;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.entityTable.getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        StringBuilder caseWhenSql = converter.toSqlPart(Converter.Type.UPDATE, new StringBuilder(), configuration,
                this.caseWhen, mybatisParamHolder);
        return this.table.getAlias() + "." + keywordQM + column + keywordQM + " = " + caseWhenSql.toString();
    }
}
