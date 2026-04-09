package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Setter
@Getter
@Accessors(chain = true)
public class Join implements SqlStruct {
    /**
     * 关联类型
     */
    private JoinType joinType;
    /**
     * 被join表
     */
    private Table joinTable;
    /**
     * 链表条件
     */
    private List<Condition> onConditions;
    /**
     * 关联表
     */
    private List<Join> joins;

    public static Join build(JoinType joinType, Table joinTable, Consumer<JoinBuilder> jbc) {
        JoinBuilder builder = new JoinBuilder(joinType, joinTable);
        jbc.accept(builder);
        return builder.build();
    }

    public static class JoinBuilder extends ConditionBuilder<JoinBuilder> {
        private final Join join;

        private JoinBuilder(JoinType joinType, Table joinTable) {
            super(new LinkedList<>());
            this.join = new Join();
            this.join.setJoinType(joinType);
            this.join.setJoinTable(joinTable);
            this.join.setOnConditions(this.conditions);
        }

        public void join(boolean sure, JoinType joinType, Table joinTable, Consumer<JoinBuilder> jbc) {
            if (sure) {
                if (this.join.getJoins() == null) {
                    this.join.setJoins(new LinkedList<>());
                }
                JoinBuilder sonBuilder = new JoinBuilder(joinType, joinTable);
                this.join.getJoins().add(sonBuilder.getJoin());
                jbc.accept(sonBuilder);
            }
        }

        public void join(JoinType joinType, Table joinTable, Consumer<JoinBuilder> jbc) {
            this.join(Boolean.TRUE, joinType, joinTable, jbc);
        }

        public void join(boolean sure, Table joinTable, Consumer<JoinBuilder> jbc) {
            this.join(sure, JoinType.InnerJoin, joinTable, jbc);
        }

        public void join(Table joinTable, Consumer<JoinBuilder> jbc) {
            this.join(Boolean.TRUE, JoinType.InnerJoin, joinTable, jbc);
        }

        private Join getJoin() {
            return this.join;
        }

        private Join build() {
            return this.join;
        }
    }
}
