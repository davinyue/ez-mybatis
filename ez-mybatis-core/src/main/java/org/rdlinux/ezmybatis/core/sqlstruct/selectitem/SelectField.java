package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

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
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String columnName = keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName() + keywordQM;
        String sql = " " + this.table.getAlias() + "." + columnName + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
