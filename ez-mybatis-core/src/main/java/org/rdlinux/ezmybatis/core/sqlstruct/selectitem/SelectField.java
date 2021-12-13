package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

public class SelectField extends AbstractSelectItem {
    protected Table table;
    protected String field;

    public SelectField(Table table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    public SelectField(Table table, String field, String alias) {
        this(table, field);
        this.setAlias(alias);
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
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
        String columnName = keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName() + keywordQM;
        String sql = " " + this.table.getAlias() + "." + columnName + " ";
        String alias = this.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sql;
    }
}
