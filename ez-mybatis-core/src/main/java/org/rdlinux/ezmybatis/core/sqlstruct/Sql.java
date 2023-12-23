package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.utils.Assert;

/**
 * sql
 */
public class Sql implements MultipleRetOperand {
    private String sql;

    private Sql(String sql) {
        Assert.notNull(sql, "sql can not be null");
        this.sql = sql;
    }

    public static Sql of(String function) {
        return new Sql(function);
    }

    public String getSql() {
        return this.sql;
    }
}
