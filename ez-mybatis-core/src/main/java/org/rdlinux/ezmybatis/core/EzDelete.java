package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 删除语句参数对象。
 *
 * <p>用于描述 delete 语句的主表、待删除表、多表 join 以及 where 条件。</p>
 */
@Getter
public class EzDelete extends EzParam<Integer> {
    /**
     * 需要删除的表列表。
     */
    private final List<Table> deletes = new LinkedList<>();
    /**
     * 删除语句中使用的关联表。
     */
    private List<Join> joins;

    private EzDelete() {
        super(Integer.class);
    }

    /**
     * 以指定主表创建删除构造器。
     *
     * @param table 删除语句主表
     * @return 删除构造器
     */
    public static EzDeleteBuilder delete(Table table) {
        return new EzDeleteBuilder(table);
    }

    /**
     * {@link EzDelete} 构造器。
     */
    public static class EzDeleteBuilder {
        private final EzDelete delete;

        private EzDeleteBuilder(Table table) {
            this.delete = new EzDelete();
            this.delete.deletes.add(table);
            this.delete.table = table;
            this.delete.from = new From(table);
        }

        /**
         * 添加需要删除的实体表。
         *
         * @param table 需要删除的实体表
         * @return 当前构造器
         */
        public EzDeleteBuilder delete(EntityTable table) {
            this.delete.deletes.add(table);
            return this;
        }

        /**
         * 添加 join 条件。
         *
         * @param sure      是否启用当前 join
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzDeleteBuilder join(boolean sure, JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            if (sure) {
                if (this.delete.joins == null) {
                    this.delete.joins = new ArrayList<>();
                }
                Join join = Join.build(joinType, joinTable, jbc);
                this.delete.joins.add(join);
            }
            return this;
        }

        /**
         * 添加 join 条件。
         *
         * @param sure      是否启用当前 join
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzDeleteBuilder join(boolean sure, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(sure, JoinType.InnerJoin, joinTable, jbc);
        }

        /**
         * 添加 join 条件。
         *
         * @param joinType  join 类型
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzDeleteBuilder join(JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, joinType, joinTable, jbc);
        }

        /**
         * 添加 join 条件。
         *
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzDeleteBuilder join(Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, JoinType.InnerJoin, joinTable, jbc);
        }

        /**
         * 创建 where 条件构造器。
         *
         * @param sure 是否启用当前 where
         * @param wcb  当前where条件构造器
         * @return 构造器
         */
        public EzDeleteBuilder where(boolean sure, Consumer<Where.WhereBuilder> wcb) {
            if (sure) {
                Where where = this.delete.where;
                if (where == null) {
                    this.delete.where = Where.build(wcb);
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
        public EzDeleteBuilder where(Consumer<Where.WhereBuilder> wcb) {
            return this.where(Boolean.TRUE, wcb);
        }

        /**
         * 构建删除语句对象。
         *
         * @return 删除语句对象
         */
        public EzDelete build() {
            return this.delete;
        }
    }
}
