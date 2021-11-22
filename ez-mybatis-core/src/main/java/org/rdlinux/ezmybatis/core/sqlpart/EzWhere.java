package org.rdlinux.ezmybatis.core.sqlpart;

import org.rdlinux.ezmybatis.core.sqlpart.condition.EzCondition;

import java.util.List;

/**
 * where条件
 */
public class EzWhere {
    /**
     * 条件
     */
    List<EzCondition> conditions;

    public EzWhere(List<EzCondition> conditions) {
        this.conditions = conditions;
    }

    public List<EzCondition> getConditions() {
        return this.conditions;
    }

    public void setConditions(List<EzCondition> conditions) {
        this.conditions = conditions;
    }
}
