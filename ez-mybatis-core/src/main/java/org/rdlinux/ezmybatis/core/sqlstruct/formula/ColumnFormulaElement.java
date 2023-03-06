package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class ColumnFormulaElement extends AbstractFormulaElement {
    private Table table;
    private String column;

    public ColumnFormulaElement(Operator operator, Table table, String column) {
        super(operator);
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(column, "column can not be null");
        this.table = table;
        this.column = column;
    }
}
