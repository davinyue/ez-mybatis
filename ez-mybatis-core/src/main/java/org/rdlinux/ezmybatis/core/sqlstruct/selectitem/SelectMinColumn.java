package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectMinColumn extends SelectColumn implements SqlPart {


    public SelectMinColumn(Table table, String column) {
        super(table, column);
    }

    public SelectMinColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }
}
