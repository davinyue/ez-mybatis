package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectMinField extends SelectField {
    public SelectMinField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectMinField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " MIN(" + this.getTable().getAlias() + "." + keywordQM + entityClassInfo
                .getFieldInfo(this.getField()).getColumnName() + keywordQM + ") ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
