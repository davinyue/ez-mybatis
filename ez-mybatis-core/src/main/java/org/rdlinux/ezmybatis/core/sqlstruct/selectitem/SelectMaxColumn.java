package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectMaxColumn extends SelectColumn implements SqlPart {


    public SelectMaxColumn(Table table, String column) {
        super(table, column);
    }

    public SelectMaxColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }
}
