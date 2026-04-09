package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class EzQuery<Rt> extends EzParam<Rt> implements MultipleRetOperand, QueryRetNeedAlias {
    private Select select;
    private List<Join> joins;
    private GroupBy groupBy;
    private OrderBy orderBy;
    private Having having;
    private Page page;
    private List<Union> unions;
    private Limit limit;

    public ExistsCondition exists() {
        return new ExistsCondition(AndOr.AND, this, Boolean.FALSE);
    }

    public ExistsCondition orExists() {
        return new ExistsCondition(AndOr.OR, this, Boolean.FALSE);
    }

    public ExistsCondition notExists() {
        return new ExistsCondition(AndOr.AND, this, Boolean.TRUE);
    }

    public ExistsCondition orNotExists() {
        return new ExistsCondition(AndOr.OR, this, Boolean.TRUE);
    }

    private EzQuery(Class<Rt> retType) {
        super(retType);
    }

    public static <Rt> EzQueryBuilder<Rt> builder(Class<Rt> retType) {
        return new EzQueryBuilder<>(retType);
    }

    public static class EzQueryBuilder<Rt> {
        private final EzQuery<Rt> query;
        /**
         * from 表是否已经设置
         */
        private boolean fromIsSet = false;

        private EzQueryBuilder(Class<Rt> retType) {
            this.query = new EzQuery<>(retType);
        }

        public EzQueryBuilder<Rt> from(Table table) {
            this.query.table = table;
            this.query.from = new From(table);
            this.fromIsSet = true;
            return this;
        }

        /**
         * 检查from表是否已经指定
         */
        private void checkFromTable() {
            if (!this.fromIsSet) {
                throw new RuntimeException("The from table is not specified. Please call the from function to " +
                        "specify the main table to be queried first.");
            }
        }

        public EzQueryBuilder<Rt> select(boolean sure, Consumer<Select.EzSelectBuilder> sc) {
            if (sure) {
                this.checkFromTable();
                Select select = this.query.select;
                if (select == null) {
                    select = Select.build(this.query, sc);
                    this.query.select = select;
                } else {
                    Select.build(this.query, select.getSelectItems(), sc);
                }
            }
            return this;
        }

        public EzQueryBuilder<Rt> select(Consumer<Select.EzSelectBuilder> sc) {
            return this.select(Boolean.TRUE, sc);
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
        public EzQueryBuilder<Rt> join(boolean sure, JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            if (sure) {
                if (this.query.getJoins() == null) {
                    this.query.joins = new ArrayList<>();
                }
                Join join = Join.build(joinType, joinTable, jbc);
                this.query.joins.add(join);
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
        public EzQueryBuilder<Rt> join(boolean sure, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
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
        public EzQueryBuilder<Rt> join(JoinType joinType, Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, joinType, joinTable, jbc);
        }

        /**
         * 添加 join 表。
         *
         * @param joinTable 被关联表
         * @param jbc       join 条件构造回调
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> join(Table joinTable, Consumer<Join.JoinBuilder> jbc) {
            return this.join(Boolean.TRUE, JoinType.InnerJoin, joinTable, jbc);
        }


        /**
         * 创建 where 条件构造器。
         *
         * @param sure 是否启用当前 where
         * @param wcb  当前where条件构造器
         * @return 构造器
         */
        public EzQueryBuilder<Rt> where(boolean sure, Consumer<Where.WhereBuilder> wcb) {
            if (sure) {
                Where where = this.query.where;
                if (where == null) {
                    this.query.where = Where.build(wcb);
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
        public EzQueryBuilder<Rt> where(Consumer<Where.WhereBuilder> wcb) {
            return this.where(Boolean.TRUE, wcb);
        }

        public EzQueryBuilder<Rt> groupBy(boolean sure, Consumer<GroupBy.GroupBuilder> gpc) {
            if (sure) {
                this.checkFromTable();
                GroupBy groupBy = this.query.groupBy;
                if (groupBy == null) {
                    groupBy = GroupBy.build(gpc);
                    this.query.groupBy = groupBy;
                } else {
                    GroupBy.build(groupBy.getItems(), gpc);
                }
            }
            return this;
        }

        public EzQueryBuilder<Rt> groupBy(Consumer<GroupBy.GroupBuilder> gpc) {
            return this.groupBy(Boolean.TRUE, gpc);
        }

        public EzQueryBuilder<Rt> orderBy(boolean sure, Consumer<OrderBy.OrderBuilder> odc) {
            if (sure) {
                this.checkFromTable();
                OrderBy orderBy = this.query.orderBy;
                if (orderBy == null) {
                    orderBy = OrderBy.build(this.query, odc);
                    this.query.orderBy = orderBy;
                } else {
                    OrderBy.build(this.query, orderBy.getItems(), odc);
                }
            }
            return this;
        }

        public EzQueryBuilder<Rt> orderBy(Consumer<OrderBy.OrderBuilder> odc) {
            return this.orderBy(Boolean.TRUE, odc);
        }

        /**
         * 创建 having 条件构造器。
         *
         * @param sure 是否启用当前 where
         * @param hc   当前having条件构造器
         * @return 构造器
         */
        public EzQueryBuilder<Rt> having(boolean sure, Consumer<Having.HavingBuilder> hc) {
            if (sure) {
                Having having = this.query.having;
                if (having == null) {
                    this.query.having = Having.build(hc);
                } else {
                    Having.build(hc, having.getConditions());
                }
            }
            return this;
        }

        /**
         * 创建 having 条件构造器。
         *
         * @param hc 当前having条件构造器
         * @return 构造器
         */
        public EzQueryBuilder<Rt> having(Consumer<Having.HavingBuilder> hc) {
            return this.having(Boolean.TRUE, hc);
        }

        /**
         * 分页
         *
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         */
        public EzQueryBuilder<Rt> page(boolean sure, int currentPage, int pageSize) {
            this.checkFromTable();
            if (sure) {
                this.query.page = new Page(this.query, (currentPage - 1) * pageSize, pageSize);
            }
            return this;
        }

        /**
         * 分页
         *
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         */
        public EzQueryBuilder<Rt> page(int currentPage, int pageSize) {
            return this.page(true, currentPage, pageSize);
        }

        /**
         * 限定, 当存在分页时, 限定将失效
         */
        public EzQueryBuilder<Rt> limit(boolean sure, int limit) {
            this.checkFromTable();
            if (sure) {
                this.query.limit = new Limit(limit);
            }
            return this;
        }

        /**
         * 限定, 当存在分页时, 限定将失效
         */
        public EzQueryBuilder<Rt> limit(int limit) {
            return this.limit(true, limit);
        }

        /**
         * 联合查询
         */
        public EzQueryBuilder<Rt> union(boolean sure, EzQuery<?> query) {
            this.checkFromTable();
            if (sure) {
                if (this.query.unions == null) {
                    this.query.unions = new LinkedList<>();
                }
                this.query.unions.add(new Union(false, query));
            }
            return this;
        }

        /**
         * 联合查询
         */
        public EzQueryBuilder<Rt> union(EzQuery<?> query) {
            return this.union(true, query);
        }

        /**
         * 联合查询
         */
        public EzQueryBuilder<Rt> unionAll(boolean sure, EzQuery<?> query) {
            this.checkFromTable();
            if (sure) {
                if (this.query.unions == null) {
                    this.query.unions = new LinkedList<>();
                }
                this.query.unions.add(new Union(true, query));
            }
            return this;
        }

        /**
         * 联合查询
         */
        public EzQueryBuilder<Rt> unionAll(EzQuery<?> query) {
            return this.unionAll(true, query);
        }

        public EzQuery<Rt> build() {
            this.checkFromTable();
            return this.query;
        }
    }
}
