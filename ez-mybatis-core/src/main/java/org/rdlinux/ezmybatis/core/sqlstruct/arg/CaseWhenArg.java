package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * CaseWhen参数
 */
public class CaseWhenArg implements Arg {
    private CaseWhen caseWhen;

    private CaseWhenArg(CaseWhen caseWhen) {
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }

    public static CaseWhenArg of(CaseWhen caseWhen) {
        return new CaseWhenArg(caseWhen);
    }

    public CaseWhen getCaseWhen() {
        return this.caseWhen;
    }
}
