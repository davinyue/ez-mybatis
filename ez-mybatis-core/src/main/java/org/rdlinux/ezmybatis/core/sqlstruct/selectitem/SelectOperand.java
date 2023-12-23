package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetNeedAlias;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

public class SelectOperand extends AbstractSelectItem implements SqlStruct {
    protected Operand operand;

    public SelectOperand(Operand operand, String alias) {
        Assert.notNull(operand, "operand can not be null");
        if (operand instanceof QueryRetNeedAlias) {
            Assert.notNull(alias, "alias can not be null");
        }
        this.operand = operand;
        this.setAlias(alias);
    }

    public Operand getOperand() {
        return this.operand;
    }
}
