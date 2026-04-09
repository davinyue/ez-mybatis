package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Having implements SqlStruct {
    /**
     * 条件
     */
    private final List<Condition> conditions;

    private Having(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static Having build(Consumer<Having.HavingBuilder> hc, List<Condition> conditions) {
        Having.HavingBuilder builder = new Having.HavingBuilder(conditions);
        hc.accept(builder);
        return builder.build();
    }

    public static Having build(Consumer<Having.HavingBuilder> hc) {
        Having.HavingBuilder builder = new Having.HavingBuilder(new ArrayList<>());
        hc.accept(builder);
        return builder.build();
    }


    public static class HavingBuilder extends ConditionBuilder<HavingBuilder> {
        private final Having having;

        private HavingBuilder(List<Condition> conditions) {
            super(conditions);
            this.having = new Having(conditions);
        }

        private Having build() {
            return this.having;
        }
    }
}
