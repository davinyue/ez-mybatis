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

/**
 * JOIN 结构节点。
 *
 * <p>用于描述 join 类型、被关联表、ON 条件以及递归子 join。</p>
 */
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

    /**
     * 构建 JOIN 结构。
     *
     * @param joinType  join 类型
     * @param joinTable 被关联表
     * @param jbc       join 条件构造回调
     * @return JOIN 结构对象
     */
    public static Join build(JoinType joinType, Table joinTable, Consumer<JoinBuilder> jbc) {
        JoinBuilder builder = new JoinBuilder(joinType, joinTable);
        jbc.accept(builder);
        return builder.build();
    }

    /**
     * {@link Join} 构造器。
     */
    public static class JoinBuilder extends ConditionBuilder<JoinBuilder> {
        /**
         * 当前构建中的 join 对象。
         */
        private final Join join;

        /**
         * 使用 join 类型和被关联表初始化构造器。
         *
         * @param joinType  join 类型
         * @param joinTable 被关联表
         */
        private JoinBuilder(JoinType joinType, Table joinTable) {
            super(new LinkedList<>());
            this.join = new Join();
            this.join.setJoinType(joinType);
            this.join.setJoinTable(joinTable);
            this.join.setOnConditions(this.conditions);
        }

        /**
         * 根据条件向当前 join 追加子 join。
         *
         * @param sure      是否启用当前 join
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       子 join 条件构造回调
         */
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

        /**
         * 追加指定类型的子 join。
         *
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       子 join 条件构造回调
         */
        public void join(JoinType joinType, Table joinTable, Consumer<JoinBuilder> jbc) {
            this.join(Boolean.TRUE, joinType, joinTable, jbc);
        }

        /**
         * 根据条件追加默认 INNER JOIN 子 join。
         *
         * @param sure      是否启用当前 join
         * @param joinTable 被关联表
         * @param jbc       子 join 条件构造回调
         */
        public void join(boolean sure, Table joinTable, Consumer<JoinBuilder> jbc) {
            this.join(sure, JoinType.InnerJoin, joinTable, jbc);
        }

        /**
         * 追加默认 INNER JOIN 子 join。
         *
         * @param joinTable 被关联表
         * @param jbc       子 join 条件构造回调
         */
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
