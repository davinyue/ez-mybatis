package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectSumColumn extends SelectColumn implements SqlStruct {


    public SelectSumColumn(Table table, String column) {
        super(table, column);
    }

    public SelectSumColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }
}
