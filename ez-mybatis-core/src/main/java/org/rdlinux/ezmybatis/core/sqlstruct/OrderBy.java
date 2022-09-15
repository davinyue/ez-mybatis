package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.order.ColumnOrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.FieldOrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.OrderType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

@Getter
@Setter
public class OrderBy implements SqlPart {
    private List<OrderItem> items;

    public OrderBy(List<OrderItem> items) {
        this.items = items;
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

        /**
         * please use {@link #addField(String)} replace
         */
        @Deprecated
        public OrderBuilder<T> add(String field) {
            return this.addField(field);
        }

        public OrderBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new FieldOrderItem((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String)} replace
         */
        @Deprecated
        public OrderBuilder<T> add(boolean sure, String field) {
            return this.addField(sure, field);
        }

        public OrderBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                this.addField(field);
            }
            return this;
        }

        public OrderBuilder<T> addColumn(String column) {
            this.orderBy.getItems().add(new ColumnOrderItem(this.table, column));
            return this;
        }

        public OrderBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        /**
         * please use {@link #addField(String, OrderType)} replace
         */
        @Deprecated
        public OrderBuilder<T> add(String field, OrderType type) {
            return this.addField(field, type);
        }

        public OrderBuilder<T> addField(String field, OrderType type) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new FieldOrderItem((EntityTable) this.table, field, type));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String, OrderType)} replace
         */
        @Deprecated
        public OrderBuilder<T> add(boolean sure, String field, OrderType type) {
            return this.addField(sure, field, type);
        }

        public OrderBuilder<T> addField(boolean sure, String field, OrderType type) {
            if (sure) {
                this.addField(field, type);
            }
            return this;
        }

        public OrderBuilder<T> addColumn(String column, OrderType type) {
            this.orderBy.getItems().add(new ColumnOrderItem(this.table, column, type));
            return this;
        }

        public OrderBuilder<T> addColumn(boolean sure, String column, OrderType type) {
            if (sure) {
                return this.addColumn(column, type);
            }
            return this;
        }

        public T done() {
            return this.target;
        }
    }
}
