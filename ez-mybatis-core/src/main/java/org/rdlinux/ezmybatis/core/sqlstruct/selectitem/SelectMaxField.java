package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectMaxField extends SelectField implements SqlStruct {
    public SelectMaxField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectMaxField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }
}
