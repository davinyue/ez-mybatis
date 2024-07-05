package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class ExistsCondition implements Condition, SqlStruct {
    private AndOr andOr;
    private EzQuery<?> query;
    private boolean isNot;

    public ExistsCondition(AndOr andOr, EzQuery<?> query, boolean isNot) {
        Assert.notNull(andOr, "andOr can not be null");
        Assert.notNull(query, "query can not be null");
        this.andOr = andOr;
        this.query = query;
        this.isNot = isNot;
    }

    @Override
    public AndOr getAndOr() {
        return this.andOr;
    }
}
