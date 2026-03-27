package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Having implements SqlStruct {
    /**
     * 条件
     */
    private final List<Condition> conditions;

    public Having(List<Condition> conditions) {
        this.conditions = conditions;
    }


    public static class HavingBuilder<Builder> extends ConditionBuilder<Builder, HavingBuilder<Builder>> {

        public HavingBuilder(Builder builder, Having having, Table table) {
            super(builder, having.getConditions(), table, table);
            this.sonBuilder = this;
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure, AndOr andOr) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            return new HavingBuilder<>(this, new Having(condition.getConditions()), this.table);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(AndOr andOr) {
            return this.groupCondition(true, andOr);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition() {
            return this.groupCondition(AndOr.AND);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, AndOr.AND);
        }
    }
}
