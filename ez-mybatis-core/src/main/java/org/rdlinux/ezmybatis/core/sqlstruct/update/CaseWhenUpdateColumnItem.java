package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class CaseWhenUpdateColumnItem extends UpdateItem {
    private String column;
    private CaseWhen caseWhen;

    public CaseWhenUpdateColumnItem(Table table, String column, CaseWhen caseWhen) {
        super(table);
        Assert.notEmpty(column, "column can not be null");
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.column = column;
        this.caseWhen = caseWhen;
    }
}
