package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class SyntaxUpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private String syntax;

    public SyntaxUpdateFieldItem(EntityTable table, String field, String syntax) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.syntax = syntax;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.entityTable.getEtType());
        String column = etInfo.getFieldInfo(this.field).getColumnName();
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.table.getAlias() + "." + keywordQM + column + keywordQM + " = " + this.syntax;
    }
}
