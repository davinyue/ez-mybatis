package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetNeedAlias;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class SelectOperand extends AbstractSelectItem implements SqlStruct {
    protected QueryRetOperand operand;

    public SelectOperand(QueryRetOperand operand, String alias) {
        Assert.notNull(operand, "operand can not be null");
        if (operand instanceof QueryRetNeedAlias) {
            Assert.notNull(alias, "alias can not be null");
        }
        this.operand = operand;
        this.setAlias(alias);
    }
}
