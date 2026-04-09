package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class EzUpdate extends EzParam<Integer> {
    private UpdateSet set;
    private List<Join> joins;

    private EzUpdate() {
        super(Integer.class);
        this.set = new UpdateSet();
    }

    public static EzUpdateBuilder update(Table table) {
        return new EzUpdateBuilder(table);
    }

    public static class EzUpdateBuilder {
        private final EzUpdate ezUpdate;

        private EzUpdateBuilder(Table table) {
            this.ezUpdate = new EzUpdate();
            this.ezUpdate.table = table;
            this.ezUpdate.from = new From(table);
            this.ezUpdate.set = new UpdateSet();
        }

        public UpdateSetBuilder<EzUpdateBuilder> set(boolean sure) {
            if (sure) {
                return new UpdateSetBuilder<>(this, this.ezUpdate.table, this.ezUpdate.set);
            } else {
                return new UpdateSetBuilder<>(this, this.ezUpdate.table, new UpdateSet());
            }
        }

        public UpdateSetBuilder<EzUpdateBuilder> set() {
            return this.set(true);
        }

        public EzUpdateBuilder set(Consumer<UpdateSetBuilder<EzUpdateBuilder>> consumer) {
            UpdateSetBuilder<EzUpdateBuilder> builder = this.set();
            consumer.accept(builder);
            return this;
        }

        public EzUpdateBuilder set(boolean sure, Consumer<UpdateSetBuilder<EzUpdateBuilder>> consumer) {
            if (sure) {
                return this.set(consumer);
            }
            return this;
        }

        /**
         * 添加 join 表。
         *
         * @param sure      是否启用当前 join
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder join(boolean sure, JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            if (sure) {
                if (this.ezUpdate.getJoins() == null) {
                    this.ezUpdate.joins = new ArrayList<>();
                }
                Join join = Join.build(joinType, joinTable, jbc);
                this.ezUpdate.joins.add(join);
            }
            return this;
        }

        /**
         * 添加 join 表。
         *
         * @param sure      是否启用当前 join
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder join(boolean sure, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(sure, JoinType.InnerJoin, joinTable, jbc);
        }

        /**
         * 添加 join 表。
         *
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder join(JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, joinType, joinTable, jbc);
        }

        /**
         * 添加 join 表。
         *
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder join(Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, JoinType.InnerJoin, joinTable, jbc);
        }


        /**
         * 创建 where 条件构造器。
         *
         * @param sure 是否启用当前 where
         * @param wcb  当前where条件构造器
         * @return 构造器
         */
        public EzUpdateBuilder where(boolean sure, Consumer<Where.WhereBuilder> wcb) {
            if (sure) {
                Where where = this.ezUpdate.where;
                if (where == null) {
                    this.ezUpdate.where = Where.build(wcb);
                } else {
                    Where.build(wcb, where.getConditions());
                }
            }
            return this;
        }

        /**
         * 创建 where 条件构造器。
         *
         * @param wcb 当前where条件构造器
         * @return 构造器
         */
        public EzUpdateBuilder where(Consumer<Where.WhereBuilder> wcb) {
            return this.where(Boolean.TRUE, wcb);
        }


        public EzUpdate build() {
            return this.ezUpdate;
        }
    }
}
