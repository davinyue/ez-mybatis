package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 函数参数
 */
public class SqlArg implements Arg {
    private String sql;

    private SqlArg(String sql) {
        Assert.notNull(sql, "sql can not be null");
        this.sql = sql;
    }

    public static SqlArg of(String function) {
        return new SqlArg(function);
    }

    public String getSql() {
        return this.sql;
    }
}
