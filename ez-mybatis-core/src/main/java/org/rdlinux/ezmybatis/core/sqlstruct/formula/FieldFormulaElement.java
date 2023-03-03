package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
@Setter
public class FieldFormulaElement extends AbstractFormulaElement {
    private Table table;
    private String filed;

    public FieldFormulaElement(Operator operator, Table table, String filed) {
        super(operator);
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(filed, "filed can not be null");
        this.table = table;
        this.filed = filed;
    }
}
