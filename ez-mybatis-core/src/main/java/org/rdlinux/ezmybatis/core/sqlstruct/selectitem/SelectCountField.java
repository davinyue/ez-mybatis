package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectCountField extends SelectField {
    /**
     * 是否去重
     */
    private boolean distinct = false;

    public SelectCountField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectCountField(EntityTable table, boolean distinct, String field) {
        super(table, field);
        this.distinct = distinct;
    }

    public SelectCountField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }

    public SelectCountField(EntityTable table, boolean distinct, String field, String alias) {
        super(table, field, alias);
        this.distinct = distinct;
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String distinctStr = "";
        if (this.distinct) {
            distinctStr = " DISTINCT ";
        }
        String sql = " COUNT(" + distinctStr + this.getTable().getAlias() + "." + keywordQM + entityClassInfo
                .getFieldInfo(this.getField()).getColumnName() + keywordQM + ") ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
