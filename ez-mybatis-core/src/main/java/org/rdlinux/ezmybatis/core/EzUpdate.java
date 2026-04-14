package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 更新语句参数对象。
 */
@Getter
public class EzUpdate extends EzParam<Integer> {
    /**
     * 更新字段集合。
     */
    private UpdateSet set;
    /**
     * 更新语句中的关联表列表。
     */
    private List<Join> joins;

    /**
     * 创建空的更新对象。
     */
    private EzUpdate() {
        super(Integer.class);
    }

    /**
     * 以指定主表创建更新构造器。
     *
     * @param table 更新主表
     * @return 更新构造器
     */
    public static EzUpdateBuilder update(Table table) {
        return new EzUpdateBuilder(table);
    }

    /**
     * {@link EzUpdate} 构造器。
     */
    public static class EzUpdateBuilder {
        /**
         * 当前构建中的更新对象。
         */
        private final EzUpdate ezUpdate;

        /**
         * 使用主表初始化更新构造器。
         *
         * @param table 更新主表
         */
        private EzUpdateBuilder(Table table) {
            this.ezUpdate = new EzUpdate();
            this.ezUpdate.table = table;
            this.ezUpdate.from = new From(table);
        }

        /**
         * 通过回调构造更新集合。
         *
         * @param consumer 更新集合构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder set(Consumer<UpdateSet.UpdateSetBuilder> consumer) {
            if (this.ezUpdate.set == null) {
                this.ezUpdate.set = UpdateSet.build(consumer);
            } else {
                UpdateSet.build(consumer, this.ezUpdate.set.getItems());
            }
            return this;
        }

        /**
         * 根据条件通过回调构造更新集合。
         *
         * @param sure     是否启用当前 set
         * @param consumer 更新集合构造回调
         * @return 当前构造器
         */
        public EzUpdateBuilder set(boolean sure, Consumer<UpdateSet.UpdateSetBuilder> consumer) {
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


        /**
         * 完成更新对象构造。
         *
         * @return 更新对象
         */
        public EzUpdate build() {
            return this.ezUpdate;
        }
    }
}
