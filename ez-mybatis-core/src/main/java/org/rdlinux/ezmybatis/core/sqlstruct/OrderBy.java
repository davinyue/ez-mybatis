package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.OrderType;

import java.util.List;

/**
 * ORDER BY 排序结构
 */
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

    /**
     * ORDER BY 构造器
     */
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

        /**
         * 添加当前实体表属性作为排序字段（保留作高频语法糖）
         *
         * @param field 实体属性名
         * @param type  排序类型 (ASC/DESC)
         */
        public OrderBuilder<T> addField(String field, OrderType type) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new OrderItem().setValue(EntityField.of((EntityTable) this.table, field))
                    .setOrderType(type));
            return this;
        }

        /**
         * 添加当前实体表属性作为升序排序字段
         *
         * @param field 实体属性名
         */
        public OrderBuilder<T> addField(String field) {
            return this.addField(field, OrderType.ASC);
        }

        /**
         * 根据条件添加当前实体表属性作为排序字段
         *
         * @param sure  是否满足条件
         * @param field 实体属性名
         * @param type  排序类型
         */
        public OrderBuilder<T> addField(boolean sure, String field, OrderType type) {
            if (sure) {
                return this.addField(field, type);
            }
            return this;
        }

        /**
         * 根据条件添加当前实体表属性作为升序排序字段
         *
         * @param sure  是否满足条件
         * @param field 实体属性名
         */
        public OrderBuilder<T> addField(boolean sure, String field) {
            return this.addField(sure, field, OrderType.ASC);
        }

        /**
         * 根据条件添加通用操作数作为排序项
         *
         * @param sure    是否满足条件
         * @param operand 操作数（如 EntityField, TableColumn, Alias, Function 等）
         * @param type    排序类型
         */
        public OrderBuilder<T> add(boolean sure, Operand operand, OrderType type) {
            if (sure) {
                this.orderBy.getItems().add(new OrderItem().setValue(operand).setOrderType(type));
            }
            return this;
        }

        /**
         * 根据条件添加通用操作数作为升序排序项
         *
         * @param sure    是否满足条件
         * @param operand 操作数
         */
        public OrderBuilder<T> add(boolean sure, Operand operand) {
            return this.add(sure, operand, OrderType.ASC);
        }

        /**
         * 添加通用操作数作为排序项
         *
         * @param operand 操作数
         * @param type    排序类型
         */
        public OrderBuilder<T> add(Operand operand, OrderType type) {
            return this.add(true, operand, type);
        }

        /**
         * 添加通用操作数作为升序排序项
         *
         * @param operand 操作数
         */
        public OrderBuilder<T> add(Operand operand) {
            return this.add(operand, OrderType.ASC);
        }

        /**
         * 根据条件添加排序项
         *
         * @param sure 是否满足条件
         * @param item 排序项
         */
        public OrderBuilder<T> add(boolean sure, OrderItem item) {
            if (sure) {
                this.orderBy.getItems().add(item);
            }
            return this;
        }

        /**
         * 添加排序项
         *
         * @param item 排序项
         */
        public OrderBuilder<T> add(OrderItem item) {
            return this.add(true, item);
        }

        /**
         * 结束 ORDER BY 构造
         */
        public T done() {
            return this.target;
        }
    }
}
