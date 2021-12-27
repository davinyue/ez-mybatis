package org.rdlinux.ezmybatis.core.sqlstruct.order;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

@Getter
public abstract class OrderItem {
    protected Table table;
    protected OrderType type;

    public OrderItem(Table table, OrderType type) {
        this.table = table;
        this.type = type;
    }

    public OrderItem(Table table) {
        this.table = table;
        this.type = OrderType.ASC;
    }

    public abstract String toSqlStruct(Configuration configuration);
}
