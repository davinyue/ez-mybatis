package org.rdlinux.ezmybatis.core.sqlpart.selectfield;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlpart.EzTable;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

public class EzNormalSelectField extends EzAbstractSelectField {
    private EzTable table;
    private String field;

    public EzNormalSelectField(EzTable table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    public EzNormalSelectField(EzTable table, String field, String alias) {
        this(table, field);
        this.setAlias(alias);
    }

    public EzTable getTable() {
        return this.table;
    }

    public void setTable(EzTable table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        Assert.notNull(field, "field can not be null");
        this.field = field;
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, this.table.getEtType());
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        String sql = " " + this.table.getAlias() + "." + keywordQM + entityClassInfo.getFieldInfo(this.field)
                .getColumnName() + keywordQM + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
