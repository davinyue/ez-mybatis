package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class CaseWhenUpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private CaseWhen caseWhen;

    public CaseWhenUpdateFieldItem(EntityTable table, String field, CaseWhen caseWhen) {
        super(table);
        Assert.notEmpty(field, "field can not be null");
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.entityTable = table;
        this.field = field;
        this.caseWhen = caseWhen;
    }
}
