package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class SelectAvgColumn extends SelectColumn implements SqlStruct {


    public SelectAvgColumn(Table table, String column) {
        super(table, column);
    }

    public SelectAvgColumn(Table table, String column, String alias) {
        super(table, column, alias);
    }
}
