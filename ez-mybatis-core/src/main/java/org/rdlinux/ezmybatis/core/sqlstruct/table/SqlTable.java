package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

@Setter
@Getter
public class SqlTable extends AbstractTable {
    private String sql;

    private SqlTable(String sql) {
        super(AliasGenerate.getAlias());
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

}
