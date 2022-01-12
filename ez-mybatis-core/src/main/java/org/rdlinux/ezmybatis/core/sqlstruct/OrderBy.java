package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.order.ColumnOrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.FieldOrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.order.OrderType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderBy implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                OrderBy.defaultOrderByToSql(sqlBuilder, configuration, (EzQuery<?>) ezParam);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    private List<OrderItem> items;

    public OrderBy(List<OrderItem> items) {
        this.items = items;
    }

    private static StringBuilder defaultOrderByToSql(StringBuilder sqlBuilder, Configuration configuration,
                                                     EzQuery<?> ezParam) {
        OrderBy order = ezParam.getOrderBy();
        if (order == null || order.getItems() == null) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" ORDER BY ");
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem orderItem = order.getItems().get(i);
                sql.append(orderItem.toSqlStruct(configuration));
                if (i + 1 < order.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sqlBuilder.append(sql);
        }
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
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

        public OrderBuilder<T> add(String field) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new FieldOrderItem((EntityTable) this.table, field));
            return this;
        }

        public OrderBuilder<T> add(boolean sure, String field) {
            if (sure) {
                this.add(field);
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

        public OrderBuilder<T> add(String field, OrderType type) {
            this.checkEntityTable();
            this.orderBy.getItems().add(new FieldOrderItem((EntityTable) this.table, field, type));
            return this;
        }

        public OrderBuilder<T> add(boolean sure, String field, OrderType type) {
            if (sure) {
                this.add(field, type);
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
