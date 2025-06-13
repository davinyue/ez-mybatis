package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.OrderType;

import java.util.List;

@Getter
@Setter
public class OrderBy implements SqlStruct {
    private EzQuery<?> query;
    private List<OrderItem> items;

    public OrderBy(EzQuery<?> query, List<OrderItem> items) {
        this.query = query;
        this.items = items;
    }

    /**
     * 排序项
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class OrderItem implements SqlStruct {
        /**
         * 值
         */
        private Operand value;
        /**
         * 排序类型
         */
        private OrderType orderType = OrderType.ASC;
    }

    public static class OrderBuilder<T> {
        private final T target;
        private final Table table;
        private final OrderBy orderBy;

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
            this.orderBy.getItems().add(new OrderItem().setValue(EntityField.of((EntityTable) this.table, field))
                    .setOrderType(type));
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
            this.orderBy.getItems().add(new OrderItem().setValue(TableColumn.of(this.table, column))
                    .setOrderType(type));
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
                this.orderBy.getItems().add(new OrderItem().setValue(Alias.of(alias)).setOrderType(type));
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

        public OrderBuilder<T> add(boolean sure, Operand operand, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(operand).setOrderType(type));
            }
            return this;
        }

        public OrderBuilder<T> add(boolean sure, Operand operand) {
            return this.add(sure, operand, OrderType.ASC);
        }

        public OrderBuilder<T> add(Operand operand, OrderType type) {
            return this.add(true, operand, type);
        }

        public OrderBuilder<T> add(Operand operand) {
            return this.add(operand, OrderType.ASC);
        }

        public T done() {
            return this.target;
        }
    }
}
