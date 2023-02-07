package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectCountField extends SelectField implements SqlStruct {
    /**
     * 是否去重
     */
    private boolean distinct = false;

    public SelectCountField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectCountField(EntityTable table, boolean distinct, String field) {
        super(table, field);
        this.distinct = distinct;
    }

    public SelectCountField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }

    public SelectCountField(EntityTable table, boolean distinct, String field, String alias) {
        super(table, field, alias);
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return this.distinct;
    }
}
