package org.rdlinux.ezmybatis.core;

import org.rdlinux.ezmybatis.core.sqlpart.*;
import org.rdlinux.ezmybatis.core.sqlpart.condition.*;

import java.util.LinkedList;
import java.util.List;

public class EzQuery {
    private EzFrom from;
    private List<EzJoin> joins;
    private EzWhere where;
    private EzGroup groupBy;
    private EzOrder orderBy;
    private EzWhere having;
    private EzLimit limit;

    private EzQuery() {
    }

    public static EzQueryBuilder builder() {
        return new EzQueryBuilder();
    }

    public EzFrom getFrom() {
        return this.from;
    }

    public List<EzJoin> getJoins() {
        return this.joins;
    }

    public EzWhere getWhere() {
        return this.where;
    }

    public EzGroup getGroupBy() {
        return this.groupBy;
    }

    public EzOrder getOrderBy() {
        return this.orderBy;
    }

    public EzWhere getHaving() {
        return this.having;
    }

    public EzLimit getLimit() {
        return this.limit;
    }

    public static class EzQueryBuilder {
        private final EzQuery query;

        private EzQueryBuilder() {
            this.query = new EzQuery();
        }

        public EzQueryBuilder from(EzTable table) {
            this.query.from = new EzFrom(table);
            return this;
        }

        public JoinBuilder join(EzTable table) {
            return new JoinBuilder(this, table);
        }

        public WhereBuilder where() {
            if (this.query.where == null) {
                this.query.where = new EzWhere(new LinkedList<>());
            }
            return new WhereBuilder(this, this.query.where);
        }

        public GroupBuilder groupBy() {
            return new GroupBuilder(this);
        }

        public OrderBuilder orderBy() {
            return new OrderBuilder(this);
        }

        public WhereBuilder having() {
            if (this.query.having == null) {
                this.query.having = new EzWhere(new LinkedList<>());
            }
            return new WhereBuilder(this, this.query.having);
        }

        public EzQueryBuilder page(int currentPage, int pageSize) {
            this.query.limit = new EzLimit((currentPage - 1) * pageSize, pageSize);
            return this;
        }

        public EzQuery build() {
            return this.query;
        }
    }

    public static class GroupBuilder {
        private EzQueryBuilder queryBuilder;
        private EzTable table;
        private EzGroup groupBy;

        public GroupBuilder(EzQueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
            this.groupBy = queryBuilder.query.groupBy;
            if (this.groupBy == null) {
                this.groupBy = new EzGroup(new LinkedList<>());
                queryBuilder.query.groupBy = this.groupBy;
            }
            this.table = queryBuilder.query.from.getTable();
        }

        public GroupBuilder add(String field) {
            this.groupBy.getItems().add(new EzGroup.GroupItem(this.table, field));
            return this;
        }

        public GroupBuilder add(EzTable table, String field) {
            this.groupBy.getItems().add(new EzGroup.GroupItem(table, field));
            return this;
        }

        public EzQueryBuilder done() {
            return this.queryBuilder;
        }
    }

    public static class OrderBuilder {
        private EzQueryBuilder queryBuilder;
        private EzTable table;
        private EzOrder orderBY;

        public OrderBuilder(EzQueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
            this.orderBY = queryBuilder.query.orderBy;
            if (this.orderBY == null) {
                this.orderBY = new EzOrder(new LinkedList<>());
                queryBuilder.query.orderBy = this.orderBY;
            }
            this.table = queryBuilder.query.from.getTable();
        }

        public OrderBuilder add(String field) {
            this.orderBY.getItems().add(new EzOrder.OrderItem(this.table, field));
            return this;
        }

        public OrderBuilder add(String field, EzOrder.OrderType type) {
            this.orderBY.getItems().add(new EzOrder.OrderItem(this.table, field, type));
            return this;
        }

        public OrderBuilder add(EzTable table, String field) {
            this.orderBY.getItems().add(new EzOrder.OrderItem(table, field));
            return this;
        }

        public OrderBuilder add(EzTable table, String field, EzOrder.OrderType type) {
            this.orderBY.getItems().add(new EzOrder.OrderItem(table, field, type));
            return this;
        }

        public EzQueryBuilder done() {
            return this.queryBuilder;
        }
    }

    public static class WhereBuilder {
        private EzQueryBuilder queryBuilder;
        private EzWhere where;

        public WhereBuilder(EzQueryBuilder queryBuilder, EzWhere where) {
            this.queryBuilder = queryBuilder;
            this.where = where;
        }

        public WhereConditionBuilder<WhereBuilder> conditions() {
            return new WhereConditionBuilder<>(this, this.where.getConditions(),
                    this.queryBuilder.query.from.getTable());
        }

        public EzQueryBuilder done() {
            return this.queryBuilder;
        }
    }

    public static class JoinBuilder {
        private EzJoin join;
        private EzQuery query;
        private EzQueryBuilder queryBuilder;

        private JoinBuilder(EzQueryBuilder queryBuilder, EzTable joinTable) {
            this.queryBuilder = queryBuilder;
            this.query = queryBuilder.query;
            this.join = new EzJoin();
            this.join.setTable(this.query.from.getTable());
            this.join.setJoinType(EzJoin.JoinType.InnerJoin);
            this.join.setJoinTable(joinTable);
            if (this.query.joins == null) {
                this.query.joins = new LinkedList<>();
            }
            this.query.joins.add(this.join);
        }

        public JoinConditionBuilder<JoinBuilder> conditions() {
            if (this.join.getOnConditions() == null) {
                this.join.setOnConditions(new LinkedList<>());
            }
            return new JoinConditionBuilder<>(this, this.join.getOnConditions(), this.join.getTable(),
                    this.join.getJoinTable());
        }

        public JoinBuilder type(EzJoin.JoinType type) {
            this.join.setJoinType(type);
            return this;
        }

        public EzQueryBuilder done() {
            return this.queryBuilder;
        }
    }

    public static class WhereConditionBuilder<Builder> {
        private Builder builder;
        private List<EzCondition> conditions;
        private EzTable table;

        public WhereConditionBuilder(Builder builder, List<EzCondition> conditions, EzTable table) {
            this.builder = builder;
            this.conditions = conditions;
            this.table = table;
        }

        public Builder done() {
            return this.builder;
        }

        public WhereConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, EzTable table,
                                                           String field, Operator operator, Object value) {
            this.conditions.add(new EzNormalCondition(loginSymbol, table, field, operator, value));
            return this;
        }

        public WhereConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, EzTable table,
                                                           String field, Object value) {
            return this.addCondition(loginSymbol, table, field, Operator.equal, value);
        }

        public WhereConditionBuilder<Builder> addCondition(EzTable table, String field,
                                                           Operator operator, Object value) {
            this.conditions.add(new EzNormalCondition(EzCondition.LoginSymbol.AND, table, field, operator, value));
            return this;
        }

        public WhereConditionBuilder<Builder> addCondition(EzTable table, String field, Object value) {
            this.conditions.add(new EzNormalCondition(EzCondition.LoginSymbol.AND, table, field, Operator.equal,
                    value));
            return this;
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public WhereConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, String field,
                                                           Operator operator, Object value) {
            this.conditions.add(new EzNormalCondition(loginSymbol, this.table, field, operator, value));
            return this;
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public WhereConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, String field,
                                                           Object value) {
            return this.addCondition(loginSymbol, field, Operator.equal, value);
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public WhereConditionBuilder<Builder> addCondition(String field, Operator operator,
                                                           Object value) {
            return this.addCondition(EzCondition.LoginSymbol.AND, field, operator, value);
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public WhereConditionBuilder<Builder> addCondition(String field, Object value) {
            return this.addCondition(EzCondition.LoginSymbol.AND, field, Operator.equal, value);
        }

        /**
         * 添加is null联表条件
         */
        public WhereConditionBuilder<Builder> addIsNullCondition(EzCondition.LoginSymbol loginSymbol, String field) {
            this.conditions.add(new EzIsNullCondition(loginSymbol, this.table, field));
            return this;
        }

        /**
         * 添加is null联表条件
         */
        public WhereConditionBuilder<Builder> addIsNullCondition(String field) {
            this.conditions.add(new EzIsNullCondition(EzCondition.LoginSymbol.AND, this.table, field));
            return this;
        }

        /**
         * 添加is not null联表条件
         */
        public WhereConditionBuilder<Builder> addIsNotNullCondition(EzCondition.LoginSymbol loginSymbol, String field) {
            this.conditions.add(new EzIsNotNullCondition(loginSymbol, this.table, field));
            return this;
        }

        /**
         * 添加is null联表条件
         */
        public WhereConditionBuilder<Builder> addIsNotNullCondition(String field) {
            this.conditions.add(new EzIsNotNullCondition(EzCondition.LoginSymbol.AND, this.table, field));
            return this;
        }

        /**
         * 添加between on联表条件
         */
        public WhereConditionBuilder<Builder> addBetweenCondition(EzCondition.LoginSymbol loginSymbol, String field,
                                                                  Object minValue, Object maxValue) {
            this.conditions.add(new EzBetweenCondition(loginSymbol, this.table, field, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加between on联表条件
         */
        public WhereConditionBuilder<Builder> addBetweenCondition(String field,
                                                                  Object minValue, Object maxValue) {
            this.conditions.add(new EzBetweenCondition(EzCondition.LoginSymbol.AND, this.table, field,
                    minValue, maxValue));
            return this;
        }

        public WhereConditionBuilder<WhereConditionBuilder<Builder>> groupCondition(
                EzCondition.LoginSymbol loginSymbol) {
            LinkedList<EzCondition> conditions = new LinkedList<>();
            EzGroupCondition condition = new EzGroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new WhereConditionBuilder<>(this, conditions, this.table);
        }

        public WhereConditionBuilder<WhereConditionBuilder<Builder>> groupCondition() {
            return this.groupCondition(EzCondition.LoginSymbol.AND);
        }
    }

    public static class JoinConditionBuilder<Builder> {
        private Builder builder;
        private List<EzCondition> conditions;
        private EzTable masterTable;
        private EzTable joinTable;

        public JoinConditionBuilder(Builder builder, List<EzCondition> conditions, EzTable masterTable,
                                    EzTable joinTable) {
            this.builder = builder;
            this.conditions = conditions;
            this.masterTable = masterTable;
            this.joinTable = joinTable;
        }

        public Builder done() {
            return this.builder;
        }

        public JoinConditionBuilder<Builder> addCondition(String masterField, String joinField) {
            return this.addCondition(masterField, Operator.equal, joinField);
        }

        public JoinConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, String masterField,
                                                          String joinField) {
            return this.addCondition(loginSymbol, masterField, Operator.equal, joinField);
        }

        public JoinConditionBuilder<Builder> addCondition(String masterField, Operator operator, String joinField) {
            return this.addCondition(EzCondition.LoginSymbol.AND, masterField, operator, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的字段
         */
        public JoinConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, String masterField,
                                                          Operator operator, String joinField) {
            this.conditions.add(new EzTableCondition(loginSymbol, this.masterTable, masterField, operator,
                    this.joinTable, joinField));
            return this;
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public JoinConditionBuilder<Builder> addCondition(EzCondition.LoginSymbol loginSymbol, String joinField,
                                                          Operator operator, Object value) {
            this.conditions.add(new EzNormalCondition(loginSymbol, this.joinTable, joinField, operator, value));
            return this;
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public JoinConditionBuilder<Builder> addCondition(String joinField, Operator operator,
                                                          Object value) {
            return this.addCondition(EzCondition.LoginSymbol.AND, joinField, operator, value);
        }

        /**
         * 添加联表条件, 使用被联表的字段
         */
        public JoinConditionBuilder<Builder> addCondition(String joinField, Object value) {
            return this.addCondition(EzCondition.LoginSymbol.AND, joinField, Operator.equal, value);
        }

        /**
         * 添加is null联表条件
         */
        public JoinConditionBuilder<Builder> addIsNullCondition(EzCondition.LoginSymbol loginSymbol, String joinField) {
            this.conditions.add(new EzIsNullCondition(loginSymbol, this.joinTable, joinField));
            return this;
        }

        /**
         * 添加is null联表条件
         */
        public JoinConditionBuilder<Builder> addIsNullCondition(String joinField) {
            this.conditions.add(new EzIsNullCondition(EzCondition.LoginSymbol.AND, this.joinTable, joinField));
            return this;
        }

        /**
         * 添加is not null联表条件
         */
        public JoinConditionBuilder<Builder> addIsNotNullCondition(EzCondition.LoginSymbol loginSymbol,
                                                                   String joinField) {
            this.conditions.add(new EzIsNotNullCondition(loginSymbol, this.joinTable, joinField));
            return this;
        }

        /**
         * 添加is null联表条件
         */
        public JoinConditionBuilder<Builder> addIsNotNullCondition(String joinField) {
            this.conditions.add(new EzIsNotNullCondition(EzCondition.LoginSymbol.AND, this.joinTable, joinField));
            return this;
        }

        /**
         * 添加between on联表条件
         */
        public JoinConditionBuilder<Builder> addBetweenCondition(EzCondition.LoginSymbol loginSymbol, String joinField,
                                                                 Object minValue, Object maxValue) {
            this.conditions.add(new EzBetweenCondition(loginSymbol, this.joinTable, joinField, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加between on联表条件
         */
        public JoinConditionBuilder<Builder> addBetweenCondition(String joinField,
                                                                 Object minValue, Object maxValue) {
            this.conditions.add(new EzBetweenCondition(EzCondition.LoginSymbol.AND, this.joinTable, joinField,
                    minValue, maxValue));
            return this;
        }

        public JoinConditionBuilder<JoinConditionBuilder<Builder>> groupCondition(EzCondition.LoginSymbol loginSymbol) {
            LinkedList<EzCondition> conditions = new LinkedList<>();
            EzGroupCondition condition = new EzGroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new JoinConditionBuilder<>(this, conditions, this.masterTable, this.joinTable);
        }

        public JoinConditionBuilder<JoinConditionBuilder<Builder>> groupCondition() {
            return this.groupCondition(EzCondition.LoginSymbol.AND);
        }
    }

}
