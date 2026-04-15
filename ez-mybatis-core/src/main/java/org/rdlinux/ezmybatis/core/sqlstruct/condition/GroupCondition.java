package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 条件分组
 */
@Getter
public class GroupCondition extends AbstractCondition implements Condition, SqlStruct {
    private final List<Condition> conditions;
    private final boolean sure;

    public GroupCondition(boolean sure, List<Condition> conditions, AndOr andOr) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(andOr, "andOr can not be null");
        this.conditions = conditions;
        this.andOr = andOr;
        this.sure = sure;
    }
}
