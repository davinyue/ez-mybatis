package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

@Getter
@Setter
public class OrderBy implements SqlStruct {
    private List<OrderItem> items;

    public OrderBy(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * 分组项
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class OrderItem implements SqlStruct {
        /**
         * 表
         */
        private Table table;
        /**
         * 值类型
         */
        private ArgType argType;
        /**
         * 值
         */
        private Object value;
        /**
         * 排序类型
         */
        private OrderType orderType = OrderType.ASC;
    }

    public static class OrderBuilder<T> {
        private T target;
        private Table table;
        private OrderBy orderBy;

        public OrderBuilder(T target, OrderBy orderBy, Table table) {
            this.target = target;
            this.orderBy = orderBy;
            this.table = table;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        public OrderBuilder<T> addField(String field, OrderType type) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new OrderItem().setTable(this.table).setValue(field)
                    .setArgType(ArgType.FILED).setOrderType(type));
            return this;
        }

        public OrderBuilder<T> addField(String field) {
            return this.addField(field, OrderType.ASC);
        }

        public OrderBuilder<T> addField(boolean sure, String field, OrderType type) {
            if (sure) {
                return this.addField(field, type);
            }
            return this;
        }


        public OrderBuilder<T> addField(boolean sure, String field) {
            return this.addField(sure, field, OrderType.ASC);
        }

        public OrderBuilder<T> addColumn(String column, OrderType type) {
            this.orderBy.getItems().add(new OrderItem().setTable(this.table).setValue(column)
                    .setArgType(ArgType.COLUMN).setOrderType(type));
            return this;
        }

        public OrderBuilder<T> addColumn(String column) {
            return this.addColumn(column, OrderType.ASC);
        }

        public OrderBuilder<T> addColumn(boolean sure, String column, OrderType type) {
            if (sure) {
                return this.addColumn(column, type);
            }
            return this;
        }

        public OrderBuilder<T> addColumn(boolean sure, String column) {
            return this.addColumn(sure, column, OrderType.ASC);
        }

        public OrderBuilder<T> addAlias(boolean sure, String alias, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(alias).setArgType(ArgType.ALIAS)
                        .setOrderType(type));
            }
            return this;
        }

        public OrderBuilder<T> addAlias(boolean sure, String alias) {
            return this.addAlias(sure, alias, OrderType.ASC);
        }

        public OrderBuilder<T> addAlias(String alias, OrderType type) {
            return this.addAlias(true, alias, type);
        }

        public OrderBuilder<T> addAlias(String alias) {
            return this.addAlias(alias, OrderType.ASC);
        }

        public OrderBuilder<T> addFormula(boolean sure, Formula formula, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(formula).setArgType(ArgType.FORMULA)
                        .setOrderType(type));
            }
            return this;
        }

        public OrderBuilder<T> addFormula(boolean sure, Formula formula) {
            return this.addFormula(sure, formula, OrderType.ASC);
        }

        public OrderBuilder<T> addFormula(Formula formula, OrderType type) {
            return this.addFormula(true, formula, type);
        }

        public OrderBuilder<T> addFormula(Formula formula) {
            return this.addFormula(formula, OrderType.ASC);
        }

        public OrderBuilder<T> addFunc(boolean sure, Function function, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(function).setArgType(ArgType.FUNC)
                        .setOrderType(type));
            }
            return this;
        }

        public OrderBuilder<T> addFunc(boolean sure, Function function) {
            return this.addFunc(sure, function, OrderType.ASC);
        }

        public OrderBuilder<T> addFunc(Function function, OrderType type) {
            return this.addFunc(true, function, type);
        }

        public OrderBuilder<T> addFunc(Function function) {
            return this.addFunc(function, OrderType.ASC);
        }

        public OrderBuilder<T> addCaseWhen(boolean sure, CaseWhen caseWhen, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(caseWhen).setArgType(ArgType.CASE_WHEN)
                        .setOrderType(type));
            }
            return this;
        }

        public OrderBuilder<T> addCaseWhen(boolean sure, CaseWhen caseWhen) {
            return this.addCaseWhen(sure, caseWhen, OrderType.ASC);
        }

        public OrderBuilder<T> addCaseWhen(CaseWhen caseWhen, OrderType type) {
            return this.addCaseWhen(true, caseWhen, type);
        }

        public OrderBuilder<T> addCaseWhen(CaseWhen caseWhen) {
            return this.addCaseWhen(caseWhen, OrderType.ASC);
        }

        public T done() {
            return this.target;
        }
    }
}
