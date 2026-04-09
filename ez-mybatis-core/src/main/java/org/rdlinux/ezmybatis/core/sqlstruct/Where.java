package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    private Where(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static Where build(Consumer<WhereBuilder> wcb, List<Condition> conditions) {
        WhereBuilder builder = new WhereBuilder(conditions);
        wcb.accept(builder);
        return builder.build();
    }

    public static Where build(Consumer<WhereBuilder> wcb) {
        return build(wcb, new ArrayList<>());
    }

    public static class WhereBuilder extends ConditionBuilder<WhereBuilder> {
        private final Where where;

        private WhereBuilder(List<Condition> conditions) {
            super(conditions);
            this.where = new Where(conditions);
        }

        private Where build() {
            return this.where;
        }
    }
}
