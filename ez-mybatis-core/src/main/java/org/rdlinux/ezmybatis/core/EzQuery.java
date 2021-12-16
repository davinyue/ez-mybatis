package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

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

        public EzQueryBuilder<Rt> from(EntityTable table) {
            this.query.table = table;
            this.query.from = new From(table);
            return this;
        }

        public Select.EzSelectBuilder<EzQueryBuilder<Rt>> select() {
            Select select = new Select(new LinkedList<>());
            this.query.select = select;
            return new Select.EzSelectBuilder<>(this, select, this.query.table);
        }

        public Join.JoinBuilder<EzQueryBuilder<Rt>> join(EntityTable joinTable) {
            if (this.query.getJoins() == null) {
                this.query.joins = new LinkedList<>();
            }
            Join join = new Join();
            this.query.joins.add(join);
            return new Join.JoinBuilder<>(this, join, this.query.table, joinTable);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> where() {
            Where where = new Where(new LinkedList<>());
            this.query.where = where;
            return new Where.WhereBuilder<>(this, where, this.query.table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder<Rt>> groupBy() {
            GroupBy group = new GroupBy(new LinkedList<>());
            this.query.groupBy = group;
            return new GroupBy.GroupBuilder<>(this, group, this.query.table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder<Rt>> orderBy() {
            OrderBy orderBy = new OrderBy(new LinkedList<>());
            this.query.orderBy = orderBy;
            return new OrderBy.OrderBuilder<>(this, orderBy, this.query.table);
        }

        public Where.WhereBuilder<EzQueryBuilder<Rt>> having() {
            Having where = new Having(new LinkedList<>());
            this.query.having = where;
            return new Where.WhereBuilder<>(this, where, this.query.table);
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
