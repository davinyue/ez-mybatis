package org.rdlinux.ezmybatis.core.sqlpart;

import java.util.List;

public class EzOrder {
    private List<OrderItem> items;

    public EzOrder(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return this.items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * 排序类型
     */
    public static enum OrderType {
        DESC,
        ASC;
    }

    public static class OrderItem {
        private EzTable table;
        private String field;
        private OrderType type;

        public OrderItem(EzTable table, String field, OrderType type) {
            this.table = table;
            this.field = field;
            this.type = type;
        }

        public OrderItem(EzTable table, String field) {
            this.table = table;
            this.field = field;
            this.type = OrderType.ASC;
        }

        public EzTable getTable() {
            return this.table;
        }

        public String getField() {
            return this.field;
        }

        public OrderType getType() {
            return this.type;
        }
    }
}
