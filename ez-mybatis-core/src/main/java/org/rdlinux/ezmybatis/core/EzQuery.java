package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.join.JoinType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzQuery<Rt> extends EzParam<Rt> {
    private Select select;
    private List<Join> joins;
    private GroupBy groupBy;
    private OrderBy orderBy;
    private Having having;
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
            this.query.select = new Select(table, new LinkedList<>());
            return this;
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select(Table table) {
            if (this.query.select == null) {
                this.query.select = new Select(table, new LinkedList<>());
            }
            return new Select.EzSelectBuilder<>(this, this.query.select, table);
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select() {
            return this.select(this.query.table);
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

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where(Table table) {
            if (this.query.where == null) {
                this.query.where = new Where(new LinkedList<>());
            }
            return new Where.WhereBuilder<>(this, this.query.where, table);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where() {
            return this.where(this.query.table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy(Table table) {
            if (this.query.groupBy == null) {
                this.query.groupBy = new GroupBy(new LinkedList<>());
            }
            return new GroupBy.GroupBuilder<>(this, this.query.groupBy, table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy() {
            return this.groupBy(this.query.table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy(Table table) {
            if (this.query.orderBy == null) {
                this.query.orderBy = new OrderBy(new LinkedList<>());
            }
            return new OrderBy.OrderBuilder<>(this, this.query.orderBy, table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy() {
            return this.orderBy(this.query.table);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having(Table table) {
            if (this.query.having == null) {
                this.query.having = new Having(new LinkedList<>());
            }
            return new Having.HavingBuilder<>(this, this.query.having, table);
        }

        public Having.HavingBuilder<EzQueryBuilder<Rt>> having() {
            return this.having(this.query.table);
        }

        /**
         * 分页
         *
         * @param currentPage 当前页, 最小为1
         * @param pageSize    页大小
         */
        public EzQueryBuilder<Rt> page(int currentPage, int pageSize) {
            this.query.limit = new Limit((currentPage - 1) * pageSize, pageSize);
            return this;
        }

        public EzQuery<Rt> build() {
            return this.query;
        }
    }
}
