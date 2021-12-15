package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzQuery extends EzParam {

    private Select select;
    private List<Join> joins;
    private GroupBy groupBy;
    private OrderBy orderBy;
    private Having having;
    private Limit limit;

    private EzQuery() {
    }

    public static EzQueryBuilder from(EntityTable table) {
        return new EzQueryBuilder(table);
    }

    public static class EzQueryBuilder {
        private final EzQuery query;

        private EzQueryBuilder(EntityTable table) {
            this.query = new EzQuery();
            this.query.table = table;
            this.query.from = new From(table);
        }

        public Select.EzSelectBuilder<EzQueryBuilder> select() {
            Select select = new Select(new LinkedList<>());
            this.query.select = select;
            return new Select.EzSelectBuilder<>(this, select, this.query.table);
        }

        public Join.JoinBuilder<EzQueryBuilder> join(EntityTable joinTable) {
            if (this.query.getJoins() == null) {
                this.query.joins = new LinkedList<>();
            }
            Join join = new Join();
            this.query.joins.add(join);
            return new Join.JoinBuilder<>(this, join, this.query.table, joinTable);
        }

        public Where.WhereBuilder<EzQueryBuilder> where() {
            Where where = new Where(new LinkedList<>());
            this.query.where = where;
            return new Where.WhereBuilder<>(this, where, this.query.table);
        }

        public GroupBy.GroupBuilder<EzQueryBuilder> groupBy() {
            GroupBy group = new GroupBy(new LinkedList<>());
            this.query.groupBy = group;
            return new GroupBy.GroupBuilder<>(this, group, this.query.table);
        }

        public OrderBy.OrderBuilder<EzQueryBuilder> orderBy() {
            OrderBy orderBy = new OrderBy(new LinkedList<>());
            this.query.orderBy = orderBy;
            return new OrderBy.OrderBuilder<>(this, orderBy, this.query.table);
        }

        public Where.WhereBuilder<EzQueryBuilder> having() {
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
        public EzQueryBuilder page(int currentPage, int pageSize) {
            this.query.limit = new Limit((currentPage - 1) * pageSize, pageSize);
            return this;
        }

        public EzQuery build() {
            return this.query;
        }
    }
}
