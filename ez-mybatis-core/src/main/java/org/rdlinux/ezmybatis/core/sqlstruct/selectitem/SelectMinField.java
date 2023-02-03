package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class SelectMinField extends SelectField implements SqlPart {
    public SelectMinField(EntityTable table, String field) {
        super(table, field);
    }

    public SelectMinField(EntityTable table, String field, String alias) {
        super(table, field, alias);
    }
}
