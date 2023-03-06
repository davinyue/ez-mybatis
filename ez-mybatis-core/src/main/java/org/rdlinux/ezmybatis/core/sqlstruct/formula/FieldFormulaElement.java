package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class FieldFormulaElement extends AbstractFormulaElement {
    private EntityTable table;
    private String filed;

    public FieldFormulaElement(Operator operator, EntityTable table, String filed) {
        super(operator);
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(filed, "filed can not be null");
        this.table = table;
        this.filed = filed;
    }
}
