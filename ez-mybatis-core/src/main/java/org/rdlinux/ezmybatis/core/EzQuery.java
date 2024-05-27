package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.LinkedList;
import java.util.List;

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

    private EzQuery(Class<Rt> retType) {
        super(retType);
    }

    public static <Rt> EzQueryBuilder<Rt> builder(Class<Rt> retType) {
        return new EzQueryBuilder<>(retType);
    }

    public static class EzQueryBuilder<Rt> {
        private final EzQuery<Rt> query;

        private EzQueryBuilder(Class<Rt> retType) {
            this.query = new EzQuery<>(retType);
        }

        public EzQueryBuilder<Rt> from(Table table) {
            this.query.table = table;
            this.query.from = new From(table);
            this.query.select = new Select(this.query, new LinkedList<>());
            return this;
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select(boolean sure, Table table) {
            Select select = this.query.select;
            if (select == null) {
                select = new Select(this.query, new LinkedList<>());
                if (sure) {
                    this.query.select = select;
                }
            } else {
                if (!sure) {
                    select = new Select(this.query, new LinkedList<>());
                }
            }
            return new Select.EzSelectBuilder<>(this, select, table);
        }


        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select(Table table) {
            return this.select(true, table);
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select(boolean sure) {
            return this.select(sure, this.query.table);
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select() {
            return this.select(true);
        }

        public Join.JoinBuilder<EzQueryBuilder<Rt>> join(boolean sure, JoinType joinType, Table joinTable) {
            if (this.query.getJoins() == null) {
                this.query.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setJoinType(joinType);
            join.setTable(this.query.table);
            join.setJoinTable(joinTable);
            join.setOnConditions(new LinkedList<>());
            join.setSure(sure);
            this.query.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }

        public Join.JoinBuilder<EzQueryBuilder<Rt>> join(JoinType joinType, Table joinTable) {
            return this.join(true, joinType, joinTable);
        }

        public Join.JoinBuilder<EzQueryBuilder<Rt>> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public Join.JoinBuilder<EzQueryBuilder<Rt>> join(boolean sure, Table joinTable) {
            return this.join(sure, JoinType.InnerJoin, joinTable);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where(boolean sure, Table table) {
            Where where = this.query.where;
            if (where == null) {
                where = new Where(new LinkedList<>());
                if (sure) {
                    this.query.where = where;
                }
            } else {
                if (!sure) {
                    where = new Where(new LinkedList<>());
                }
            }
            return new Where.WhereBuilder<>(this, where, table);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where(Table table) {
            return this.where(true, table);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where(boolean sure) {
            return this.where(sure, this.query.table);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where() {
            return this.where(true);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy(boolean sure, Table table) {
            GroupBy groupBy = this.query.groupBy;
            if (groupBy == null) {
                groupBy = new GroupBy(new LinkedList<>());
                if (sure) {
                    this.query.groupBy = groupBy;
                }
            } else {
                if (!sure) {
                    groupBy = new GroupBy(new LinkedList<>());
                }
            }
            return new GroupBy.GroupBuilder<>(this, groupBy, table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy(Table table) {
            return this.groupBy(true, table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy(boolean sure) {
            return this.groupBy(sure, this.query.table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy() {
            return this.groupBy(true);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy(boolean sure, Table table) {
            OrderBy orderBy = this.query.orderBy;
            if (orderBy == null) {
                orderBy = new OrderBy(new LinkedList<>());
                if (sure) {
                    this.query.orderBy = orderBy;
                }
            } else {
                if (!sure) {
                    orderBy = new OrderBy(new LinkedList<>());
                }
            }
            return new OrderBy.OrderBuilder<>(this, orderBy, table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy(Table table) {
            return this.orderBy(true, table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy(boolean sure) {
            return this.orderBy(sure, this.query.table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy() {
            return this.orderBy(true);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having(boolean sure, Table table) {
            Having having = this.query.having;
            if (having == null) {
                having = new Having(new LinkedList<>());
                if (sure) {
                    this.query.having = having;
                }
            } else {
                if (!sure) {
                    having = new Having(new LinkedList<>());
                }
            }
            return new Having.HavingBuilder<>(this, having, table);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having(Table table) {
            return this.having(true, table);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having(boolean sure) {
            return this.having(sure, this.query.table);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having() {
            return this.having(true);
        }

        /**
         * 分页
         *
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         */
        public EzQueryBuilder<Rt> page(boolean sure, int currentPage, int pageSize) {
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
            return this.query;
        }
    }
}
