package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectSumField extends SelectField implements SqlStruct {
    public SelectSumField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectSumField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }
}
