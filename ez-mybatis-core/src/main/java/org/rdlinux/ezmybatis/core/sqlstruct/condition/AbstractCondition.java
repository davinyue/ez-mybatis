package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public abstract class AbstractCondition implements Condition {
    protected AndOr andOr;

    @Override
    public void setAndOr(AndOr andOr) {
        Assert.notNull(andOr, "andOr can not be null");
        this.andOr = andOr;
    }
}
