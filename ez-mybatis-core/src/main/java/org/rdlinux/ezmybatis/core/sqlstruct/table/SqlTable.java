package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.utils.Assert;

public class SqlTable extends AbstractTable {
    private String sql;

    private SqlTable(String sql) {
        super(Alias.getAlias());
        Assert.notEmpty(sql, "sql can not be null");
        this.sql = sql;
    }

    public static SqlTable of(String sql) {
        return new SqlTable(sql);
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    @Override
    public String getSchema(Configuration configuration) {
        return null;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
