package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * sql
 */
@Getter
public class Sql implements MultipleRetOperand, QueryRetNeedAlias {
    private final String sql;

    private Sql(String sql) {
        Assert.notNull(sql, "sql can not be null");
        this.sql = sql;
    }

    public static Sql of(String sql) {
        return new Sql(sql);
    }
}
