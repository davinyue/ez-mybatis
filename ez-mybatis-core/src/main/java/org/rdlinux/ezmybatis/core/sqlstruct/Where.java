package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;

import java.util.LinkedList;
import java.util.List;

/**
 * where条件
 */
@Getter
@Setter
public class Where implements SqlStruct {
    /**
     * 条件
     */
    private List<Condition> conditions;

    public Where(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static class WhereBuilder<Builder> extends ConditionBuilder<Builder,
            WhereBuilder<Builder>> {

        public WhereBuilder(Builder builder, Where where, Table table) {
            super(builder, where.getConditions(), table, table);
            this.sonBuilder = this;
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition(boolean sure, AndOr andOr) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            return new WhereBuilder<>(this, new Where(condition.getConditions()), this.table);
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition(AndOr andOr) {
            return this.groupCondition(true, andOr);
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, AndOr.AND);
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition() {
            return this.groupCondition(true);
        }
    }

}
