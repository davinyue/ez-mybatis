package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Setter
@Getter
@Accessors(chain = true)
public class Join implements SqlStruct {

    /**
     * 主表
     */
    private Table table;
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
     * 是否确认连表
     */
    private boolean sure;

    public static class JoinBuilder<Builder> extends ConditionBuilder<Builder,
            JoinBuilder<Builder>> {
        private Join join;

        public JoinBuilder(Builder builder, Join join) {
            super(builder, join.getOnConditions(), join.getTable(), join.getJoinTable());
            this.sonBuilder = this;
            this.join = join;
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(boolean sure, AndOr andOr) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            Join newJoin = new Join();
            newJoin.setTable(this.join.getTable());
            newJoin.setJoinTable(this.join.getJoinTable());
            newJoin.setOnConditions(condition.getConditions());
            return new JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(AndOr andOr) {
            return this.groupCondition(true, andOr);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition() {
            return this.groupCondition(AndOr.AND);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, AndOr.AND);
        }

        /**
         * 组条件(Lambda 闭包)
         */
        public JoinBuilder<Builder> groupCondition(Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            return this.groupCondition(true, AndOr.AND, consumer);
        }

        public JoinBuilder<Builder> groupCondition(AndOr andOr, Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            return this.groupCondition(true, andOr, consumer);
        }

        public JoinBuilder<Builder> groupCondition(boolean sure, Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            return this.groupCondition(sure, AndOr.AND, consumer);
        }

        public JoinBuilder<Builder> groupCondition(boolean sure, AndOr andOr, Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            if (sure) {
                JoinBuilder<JoinBuilder<Builder>> childBuilder = this.groupCondition(true, andOr);
                consumer.accept(childBuilder);
            }
            return this;
        }

        public JoinBuilder<JoinBuilder<Builder>> join(boolean sure, JoinType joinType, Table joinTable) {
            if (this.join.getJoins() == null) {
                this.join.joins = new LinkedList<>();
            }
            Join newJoin = new Join();
            newJoin.setJoinType(joinType);
            newJoin.setTable(this.join.getJoinTable());
            newJoin.setJoinTable(joinTable);
            newJoin.setOnConditions(new LinkedList<>());
            newJoin.setSure(sure);
            this.join.joins.add(newJoin);
            return new Join.JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(JoinType joinType, Table joinTable) {
            return this.join(true, joinType, joinTable);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(boolean sure, Table joinTable) {
            return this.join(sure, JoinType.InnerJoin, joinTable);
        }

        public JoinBuilder<Builder> join(Table joinTable, Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            JoinBuilder<JoinBuilder<Builder>> builder = this.join(joinTable);
            consumer.accept(builder);
            return this;
        }

        public JoinBuilder<Builder> join(JoinType joinType, Table joinTable,
                                         Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            JoinBuilder<JoinBuilder<Builder>> builder = this.join(joinType, joinTable);
            consumer.accept(builder);
            return this;
        }

        public JoinBuilder<Builder> join(boolean sure, Table joinTable,
                                         Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            if (sure) {
                return this.join(joinTable, consumer);
            }
            return this;
        }

        public JoinBuilder<Builder> join(boolean sure, JoinType joinType, Table joinTable,
                                         Consumer<JoinBuilder<JoinBuilder<Builder>>> consumer) {
            if (sure) {
                return this.join(joinType, joinTable, consumer);
            }
            return this;
        }

        /**
         * 将被join表设置为条件构造表
         */
        public JoinBuilder<Builder> joinTableCondition() {
            this.table = this.join.getJoinTable();
            this.otherTable = this.join.getTable();
            return this;
        }

        /**
         * 将被主表设置为条件构造表
         */
        public JoinBuilder<Builder> masterTableCondition() {
            this.table = this.join.getTable();
            this.otherTable = this.join.getJoinTable();
            return this;
        }
    }
}
