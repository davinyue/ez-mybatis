package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class SelectCaseWhen extends AbstractSelectItem implements SqlStruct {
    private CaseWhen caseWhen;

    public SelectCaseWhen(CaseWhen caseWhen, String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.setAlias(alias);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }
}
