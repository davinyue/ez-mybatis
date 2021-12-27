package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import org.apache.ibatis.session.Configuration;

public class SelectField extends AbstractSelectItem {
    protected EntityTable table;
    protected String field;

    public SelectField(EntityTable table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    public SelectField(EntityTable table, String field, String alias) {
        this(table, field);
        this.setAlias(alias);
    }

    public EntityTable getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
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
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        String columnName = keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName() + keywordQM;
        String sql = " " + this.table.getAlias() + "." + columnName + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
