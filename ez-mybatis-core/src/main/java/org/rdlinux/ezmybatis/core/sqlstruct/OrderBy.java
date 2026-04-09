package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.enumeration.OrderType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * ORDER BY 排序结构
 */
@Getter
@Setter
public class OrderBy implements SqlStruct {
    /**
     * 所属查询对象。
     */
    private EzQuery<?> query;
    /**
     * 排序项列表。
     */
    private List<OrderItem> items;

    /**
     * 使用查询对象和排序项初始化 ORDER BY 结构。
     *
     * @param query 所属查询对象
     * @param items 排序项列表
     */
    private OrderBy(EzQuery<?> query, List<OrderItem> items) {
        this.query = query;
        this.items = items;
    }

    public static OrderBy build(EzQuery<?> query, Consumer<OrderBuilder> gpc) {
        OrderBuilder builder = new OrderBuilder(query);
        gpc.accept(builder);
        return builder.build();
    }

    public static OrderBy build(EzQuery<?> query, List<OrderItem> items, Consumer<OrderBuilder> gpc) {
        OrderBuilder builder = new OrderBuilder(query, items);
        gpc.accept(builder);
        return builder.build();
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
    public static class OrderBuilder {
        /**
         * 当前构建中的 ORDER BY 对象。
         */
        private final OrderBy orderBy;

        /**
         * 使用查询对象和已有排序项初始化构造器。
         *
         * @param query 所属查询对象
         * @param items 排序项列表
         */
        private OrderBuilder(EzQuery<?> query, List<OrderItem> items) {
            this.orderBy = new OrderBy(query, items);
        }

        /**
         * 使用查询对象创建空的排序构造器。
         *
         * @param query 所属查询对象
         */
        private OrderBuilder(EzQuery<?> query) {
            this.orderBy = new OrderBy(query, new ArrayList<>());
        }

        /**
         * 根据条件添加通用操作数作为排序项
         *
         * @param sure    是否满足条件
         * @param operand 操作数（如 EntityField, TableColumn, Alias, Function 等）
         * @param type    排序类型
         */
        public OrderBuilder add(boolean sure, Operand operand, OrderType type) {
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
        public OrderBuilder add(boolean sure, Operand operand) {
            return this.add(sure, operand, OrderType.ASC);
        }

        /**
         * 添加通用操作数作为排序项
         *
         * @param operand 操作数
         * @param type    排序类型
         */
        public OrderBuilder add(Operand operand, OrderType type) {
            return this.add(true, operand, type);
        }

        /**
         * 添加通用操作数作为升序排序项
         *
         * @param operand 操作数
         */
        public OrderBuilder add(Operand operand) {
            return this.add(operand, OrderType.ASC);
        }

        /**
         * 根据条件添加排序项
         *
         * @param sure 是否满足条件
         * @param item 排序项
         */
        public OrderBuilder add(boolean sure, OrderItem item) {
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
        public OrderBuilder add(OrderItem item) {
            return this.add(true, item);
        }

        /**
         * 结束 ORDER BY 构造
         */
        private OrderBy build() {
            return this.orderBy;
        }
    }
}
