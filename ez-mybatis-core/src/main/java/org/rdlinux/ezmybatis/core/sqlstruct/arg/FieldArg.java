package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 属性参数
 */
public class FieldArg implements Arg {
    private EntityTable table;
    private String field;

    private FieldArg(EntityTable table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    public static FieldArg of(EntityTable table, String field) {
        return new FieldArg(table, field);
    }

    public String getField() {
        return this.field;
    }

    public EntityTable getTable() {
        return this.table;
    }
}
