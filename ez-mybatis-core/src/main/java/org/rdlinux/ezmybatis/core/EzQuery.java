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

/**
 * 查询语句参数对象。
 *
 * @param <Rt> 查询结果类型
 */
@Getter
public class EzQuery<Rt> extends EzParam<Rt> implements MultipleRetOperand, QueryRetNeedAlias {
    /**
     * 查询列结构。
     */
    private Select select;
    /**
     * 关联表列表。
     */
    private List<Join> joins;
    /**
     * 分组结构。
     */
    private GroupBy groupBy;
    /**
     * 排序结构。
     */
    private OrderBy orderBy;
    /**
     * 分组过滤条件。
     */
    private Having having;
    /**
     * 分页结构。
     */
    private Page page;
    /**
     * UNION / UNION ALL 查询列表。
     */
    private List<Union> unions;
    /**
     * 限定返回条数的结构。
     */
    private Limit limit;

    /**
     * 构造 EXISTS 条件。
     *
     * @return EXISTS 条件对象
     */
    public ExistsCondition exists() {
        return new ExistsCondition(AndOr.AND, this, Boolean.FALSE);
    }

    /**
     * 构造 OR EXISTS 条件。
     *
     * @return EXISTS 条件对象
     */
    public ExistsCondition orExists() {
        return new ExistsCondition(AndOr.OR, this, Boolean.FALSE);
    }

    /**
     * 构造 NOT EXISTS 条件。
     *
     * @return NOT EXISTS 条件对象
     */
    public ExistsCondition notExists() {
        return new ExistsCondition(AndOr.AND, this, Boolean.TRUE);
    }

    /**
     * 构造 OR NOT EXISTS 条件。
     *
     * @return NOT EXISTS 条件对象
     */
    public ExistsCondition orNotExists() {
        return new ExistsCondition(AndOr.OR, this, Boolean.TRUE);
    }

    /**
     * 使用返回类型初始化查询对象。
     *
     * @param retType 查询结果类型
     */
    private EzQuery(Class<Rt> retType) {
        super(retType);
    }

    /**
     * 创建查询构造器。
     *
     * @param retType 返回结果类型
     * @param <Rt>    查询结果类型
     * @return 查询构造器
     */
    public static <Rt> EzQueryBuilder<Rt> builder(Class<Rt> retType) {
        return new EzQueryBuilder<>(retType);
    }

    /**
     * {@link EzQuery} 构造器。
     *
     * @param <Rt> 查询结果类型
     */
    public static class EzQueryBuilder<Rt> {
        /**
         * 当前构建中的查询对象。
         */
        private final EzQuery<Rt> query;
        /**
         * from 表是否已经设置
         */
        private boolean fromIsSet = false;

        /**
         * 使用返回类型初始化查询构造器。
         *
         * @param retType 查询结果类型
         */
        private EzQueryBuilder(Class<Rt> retType) {
            this.query = new EzQuery<>(retType);
        }

        /**
         * 指定查询主表。
         *
         * @param table 查询主表
         * @return 当前构造器
         */
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

        /**
         * 根据条件构造查询列。
         *
         * @param sure 是否启用当前 select
         * @param sc   查询列构造回调
         * @return 当前构造器
         */
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

        /**
         * 构造查询列。
         *
         * @param sc 查询列构造回调
         * @return 当前构造器
         */
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

        /**
         * 根据条件构造分组项。
         *
         * @param sure 是否启用当前 group by
         * @param gpc  分组项构造回调
         * @return 当前构造器
         */
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

        /**
         * 构造分组项。
         *
         * @param gpc 分组项构造回调
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> groupBy(Consumer<GroupBy.GroupBuilder> gpc) {
            return this.groupBy(Boolean.TRUE, gpc);
        }

        /**
         * 根据条件构造排序项。
         *
         * @param sure 是否启用当前 order by
         * @param odc  排序项构造回调
         * @return 当前构造器
         */
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

        /**
         * 构造排序项。
         *
         * @param odc 排序项构造回调
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> orderBy(Consumer<OrderBy.OrderBuilder> odc) {
            return this.orderBy(Boolean.TRUE, odc);
        }

        /**
         * 创建 having 条件构造器。
         *
         * @param sure 是否启用当前 having
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
         * 根据条件设置分页。
         *
         * @param sure        是否启用当前分页
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> page(boolean sure, int currentPage, int pageSize) {
            this.checkFromTable();
            if (sure) {
                this.query.page = new Page(this.query, (currentPage - 1) * pageSize, pageSize);
            }
            return this;
        }

        /**
         * 设置分页。
         *
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> page(int currentPage, int pageSize) {
            return this.page(true, currentPage, pageSize);
        }

        /**
         * 根据条件设置返回条数上限，当存在分页时该配置失效。
         *
         * @param sure  是否启用当前 limit
         * @param limit 最大返回条数
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> limit(boolean sure, int limit) {
            this.checkFromTable();
            if (sure) {
                this.query.limit = new Limit(limit);
            }
            return this;
        }

        /**
         * 设置返回条数上限，当存在分页时该配置失效。
         *
         * @param limit 最大返回条数
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> limit(int limit) {
            return this.limit(true, limit);
        }

        /**
         * 根据条件追加 UNION 查询。
         *
         * @param sure  是否启用当前 union
         * @param query 被联合的查询对象
         * @return 当前构造器
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
         * 追加 UNION 查询。
         *
         * @param query 被联合的查询对象
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> union(EzQuery<?> query) {
            return this.union(true, query);
        }

        /**
         * 根据条件追加 UNION ALL 查询。
         *
         * @param sure  是否启用当前 union all
         * @param query 被联合的查询对象
         * @return 当前构造器
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
         * 追加 UNION ALL 查询。
         *
         * @param query 被联合的查询对象
         * @return 当前构造器
         */
        public EzQueryBuilder<Rt> unionAll(EzQuery<?> query) {
            return this.unionAll(true, query);
        }

        /**
         * 完成查询对象构造。
         *
         * @return 查询对象
         */
        public EzQuery<Rt> build() {
            this.checkFromTable();
            if (this.query.select == null) {
                this.select(Select.EzSelectBuilder::addAll);
            }
            if (this.query.orderBy == null) {
                this.query.orderBy = OrderBy.build(this.query, orderBuilder -> {
                });
            }
            return this.query;
        }
    }
}
