package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 条件分组
 */
public class GroupCondition implements Condition, SqlStruct {
    private List<Condition> conditions;
    private AndOr andOr;
    private boolean sure;

    public GroupCondition(boolean sure, List<Condition> conditions, AndOr andOr) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(andOr, "andOr can not be null");
        this.conditions = conditions;
        this.andOr = andOr;
        this.sure = sure;
    }

    @Override
    public AndOr getAndOr() {
        return this.andOr;
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public boolean isSure() {
        return this.sure;
    }
}
