package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
@Setter
public class SelectColumn extends AbstractSelectItem implements SqlPart {
    protected Table table;
    protected String column;

    public SelectColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    public SelectColumn(Table table, String column, String alias) {
        this(table, column);
        this.setAlias(alias);
    }
}
