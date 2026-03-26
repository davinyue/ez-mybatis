package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.OrderType;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * 窗口函数
 */
@Getter
@Setter
public class WindowFunction implements QueryRetNeedAlias {

    private Table table;

    /**
     * 基础函数，例如 ROW_NUMBER(), SUM() 等
     */
    private Function function;

    /**
     * PARTITION BY 元素
     */
    private List<Operand> partitionBy;

    /**
     * ORDER BY 元素
     */
    private List<OrderBy.OrderItem> orderBy;

    /**
     * 窗口帧类型: ROWS 或 RANGE
     */
    private WindowFrameType frameType;

    /**
     * 窗口帧起始
     */
    private WindowFrameBound frameStart;

    /**
     * 窗口帧结束
     */
    private WindowFrameBound frameEnd;

    private WindowFunction() {
    }

    /**
     * 获取窗口函数构造器
     *
     * @param table    所属表
     * @param function 聚合函数或窗口控制函数 (例如 ROW_NUMBER(), SUM() 等)
     * @return 窗口函数构造器
     */
    public static WindowFunctionBuilder builder(Table table, Function function) {
        return new WindowFunctionBuilder(table, function);
    }



    /**
     * 窗口帧类型
     */
    public enum WindowFrameType {
        /**
         * 行数限定
         */
        ROWS,
        /**
         * 范围限定
         */
        RANGE
    }

    /**
     * 窗口帧边界
     */
    @Getter
    public static class WindowFrameBound {
        /**
         * 边界类型
         */
        private final BoundType type;
        /**
         * 偏移量 (例如 n PRECEDING 或 n FOLLOWING 中的 n)
         */
        private final Integer offset;

        private WindowFrameBound(BoundType type, Integer offset) {
            this.type = type;
            this.offset = offset;
        }

        /**
         * 从分区第一行开始 (UNBOUNDED PRECEDING)
         */
        public static WindowFrameBound unboundedPreceding() {
            return new WindowFrameBound(BoundType.UNBOUNDED_PRECEDING, null);
        }

        /**
         * 当前行往前 n 行/范围 (n PRECEDING)
         *
         * @param n 偏移量
         */
        public static WindowFrameBound preceding(int n) {
            return new WindowFrameBound(BoundType.PRECEDING, n);
        }

        /**
         * 当前行 (CURRENT ROW)
         */
        public static WindowFrameBound currentRow() {
            return new WindowFrameBound(BoundType.CURRENT_ROW, null);
        }

        /**
         * 当前行往后 n 行/范围 (n FOLLOWING)
         *
         * @param n 偏移量
         */
        public static WindowFrameBound following(int n) {
            return new WindowFrameBound(BoundType.FOLLOWING, n);
        }

        /**
         * 到分区最后一行 (UNBOUNDED FOLLOWING)
         */
        public static WindowFrameBound unboundedFollowing() {
            return new WindowFrameBound(BoundType.UNBOUNDED_FOLLOWING, null);
        }

        /**
         * 边界类型枚举
         */
        @Getter
        public enum BoundType {
            /**
             * 对应 UNBOUNDED PRECEDING (从分区第一行开始)
             */
            UNBOUNDED_PRECEDING("UNBOUNDED PRECEDING"),
            /**
             * 对应 n PRECEDING (当前行往前 n 行/范围)
             */
            PRECEDING("PRECEDING"),
            /**
             * 对应 CURRENT ROW (当前行)
             */
            CURRENT_ROW("CURRENT ROW"),
            /**
             * 对应 n FOLLOWING (当前行往后 n 行/范围)
             */
            FOLLOWING("FOLLOWING"),
            /**
             * 对应 UNBOUNDED FOLLOWING (到分区最后一行)
             */
            UNBOUNDED_FOLLOWING("UNBOUNDED FOLLOWING");

            /**
             * 对应的 SQL 关键字
             */
            private final String sql;

            BoundType(String sql) {
                this.sql = sql;
            }

        }
    }

    /**
     * 窗口函数构造器
     */
    public static class WindowFunctionBuilder {
        private final WindowFunction windowFunction;
        private final Table table;

        /**
         * 构造窗口函数的Builder
         *
         * @param table    关联的实体表
         * @param function 内部包裹的具体函数实例
         */
        public WindowFunctionBuilder(Table table, Function function) {
            Assert.notNull(function, "function can not be null");
            this.table = table;
            this.windowFunction = new WindowFunction();
            this.windowFunction.setTable(table);
            this.windowFunction.setFunction(function);
            this.windowFunction.setPartitionBy(new LinkedList<>());
            this.windowFunction.setOrderBy(new LinkedList<>());
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        // --- PARTITION BY ---

        /**
         * 可选添加字段为 PARTITION BY 元素
         *
         * @param sure  是否确定添加
         * @param field 实体属性名称
         */
        public WindowFunctionBuilder partitionByField(boolean sure, String field) {
            if (sure) {
                this.checkEntityTable();
                this.windowFunction.getPartitionBy().add(EntityField.of((EntityTable) this.table, field));
            }
            return this;
        }

        /**
         * 添加字段为 PARTITION BY 元素
         *
         * @param field 实体属性名称
         */
        public WindowFunctionBuilder partitionByField(String field) {
            return this.partitionByField(true, field);
        }

        /**
         * 可选添加列为 PARTITION BY 元素
         *
         * @param sure   是否确定添加
         * @param column 数据库列名
         */
        public WindowFunctionBuilder partitionByColumn(boolean sure, String column) {
            if (sure) {
                this.windowFunction.getPartitionBy().add(TableColumn.of(this.table, column));
            }
            return this;
        }

        /**
         * 添加列为 PARTITION BY 元素
         *
         * @param column 数据库列名
         */
        public WindowFunctionBuilder partitionByColumn(String column) {
            return this.partitionByColumn(true, column);
        }

        /**
         * 可选添加操作数作为 PARTITION BY 元素
         *
         * @param sure    是否确定添加
         * @param operand 操作数
         */
        public WindowFunctionBuilder partitionBy(boolean sure, Operand operand) {
            if (sure) {
                this.windowFunction.getPartitionBy().add(operand);
            }
            return this;
        }

        /**
         * 添加操作数作为 PARTITION BY 元素
         *
         * @param operand 操作数
         */
        public WindowFunctionBuilder partitionBy(Operand operand) {
            return this.partitionBy(true, operand);
        }

        // --- ORDER BY ---

        /**
         * 可选添加字段排序
         *
         * @param sure  是否确定添加
         * @param field 属性名
         * @param type  排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByField(boolean sure, String field, OrderType type) {
            if (sure) {
                this.checkEntityTable();
                this.windowFunction.getOrderBy().add(new OrderBy.OrderItem()
                        .setValue(EntityField.of((EntityTable) this.table, field)).setOrderType(type));
            }
            return this;
        }

        /**
         * 添加字段排序
         *
         * @param field 属性名
         * @param type  排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByField(String field, OrderType type) {
            return this.orderByField(true, field, type);
        }

        /**
         * 可选添加字段升序排序
         *
         * @param sure  是否确定添加
         * @param field 属性名
         */
        public WindowFunctionBuilder orderByField(boolean sure, String field) {
            return this.orderByField(sure, field, OrderType.ASC);
        }

        /**
         * 添加字段升序排序
         *
         * @param field 属性名
         */
        public WindowFunctionBuilder orderByField(String field) {
            return this.orderByField(true, field, OrderType.ASC);
        }

        /**
         * 可选添加列名排序
         *
         * @param sure   是否确定添加
         * @param column 数据库列名
         * @param type   排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByColumn(boolean sure, String column, OrderType type) {
            if (sure) {
                this.windowFunction.getOrderBy().add(new OrderBy.OrderItem()
                        .setValue(TableColumn.of(this.table, column)).setOrderType(type));
            }
            return this;
        }

        /**
         * 添加列名排序
         *
         * @param column 数据库列名
         * @param type   排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByColumn(String column, OrderType type) {
            return this.orderByColumn(true, column, type);
        }

        /**
         * 可选添加列名升序排序
         *
         * @param sure   是否确定添加
         * @param column 数据库列名
         */
        public WindowFunctionBuilder orderByColumn(boolean sure, String column) {
            return this.orderByColumn(sure, column, OrderType.ASC);
        }

        /**
         * 添加列名升序排序
         *
         * @param column 数据库列名
         */
        public WindowFunctionBuilder orderByColumn(String column) {
            return this.orderByColumn(true, column, OrderType.ASC);
        }

        /**
         * 可选添加别名排序
         *
         * @param sure  是否确定添加
         * @param alias 别名
         * @param type  排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByAlias(boolean sure, String alias, OrderType type) {
            if (sure) {
                this.windowFunction.getOrderBy().add(new OrderBy.OrderItem().setValue(Alias.of(alias))
                        .setOrderType(type));
            }
            return this;
        }

        /**
         * 添加别名排序
         *
         * @param alias 别名
         * @param type  排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderByAlias(String alias, OrderType type) {
            return this.orderByAlias(true, alias, type);
        }

        /**
         * 可选添加别名升序排序
         *
         * @param sure  是否确定添加
         * @param alias 别名
         */
        public WindowFunctionBuilder orderByAlias(boolean sure, String alias) {
            return this.orderByAlias(sure, alias, OrderType.ASC);
        }

        /**
         * 添加别名升序排序
         *
         * @param alias 别名
         */
        public WindowFunctionBuilder orderByAlias(String alias) {
            return this.orderByAlias(true, alias, OrderType.ASC);
        }

        /**
         * 可选添加操作数排序
         *
         * @param sure    是否确定添加
         * @param operand 操作数对象
         * @param type    排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderBy(boolean sure, Operand operand, OrderType type) {
            if (sure) {
                this.windowFunction.getOrderBy().add(new OrderBy.OrderItem().setValue(operand).setOrderType(type));
            }
            return this;
        }

        /**
         * 添加操作数排序
         *
         * @param operand 操作数对象
         * @param type    排序类型 (ASC/DESC)
         */
        public WindowFunctionBuilder orderBy(Operand operand, OrderType type) {
            return this.orderBy(true, operand, type);
        }

        /**
         * 可选添加操作数升序排序
         *
         * @param sure    是否确定添加
         * @param operand 操作数对象
         */
        public WindowFunctionBuilder orderBy(boolean sure, Operand operand) {
            return this.orderBy(sure, operand, OrderType.ASC);
        }

        /**
         * 添加操作数升序排序
         *
         * @param operand 操作数对象
         */
        public WindowFunctionBuilder orderBy(Operand operand) {
            return this.orderBy(true, operand, OrderType.ASC);
        }

        // --- FRAME ---

        /**
         * 设定物理行级边界：ROWS BETWEEN start AND end
         *
         * @param start 起始边界
         * @param end   结束边界
         */
        public WindowFunctionBuilder rowsBetween(WindowFrameBound start, WindowFrameBound end) {
            this.windowFunction.setFrameType(WindowFrameType.ROWS);
            this.windowFunction.setFrameStart(start);
            this.windowFunction.setFrameEnd(end);
            return this;
        }

        /**
         * 设定逻辑值范围边界：RANGE BETWEEN start AND end
         *
         * @param start 起始范围边界
         * @param end   结束范围边界
         */
        public WindowFunctionBuilder rangeBetween(WindowFrameBound start, WindowFrameBound end) {
            this.windowFunction.setFrameType(WindowFrameType.RANGE);
            this.windowFunction.setFrameStart(start);
            this.windowFunction.setFrameEnd(end);
            return this;
        }

        /**
         * 设定物理行级边界，仅配置单独的起始点 (缺省末端包含当前行): ROWS start
         *
         * @param start 起始边界
         */
        public WindowFunctionBuilder rows(WindowFrameBound start) {
            this.windowFunction.setFrameType(WindowFrameType.ROWS);
            this.windowFunction.setFrameStart(start);
            this.windowFunction.setFrameEnd(null);
            return this;
        }

        /**
         * 设定逻辑范围边界，仅配置单独的起始范围 (缺省末端包含当前行): RANGE start
         *
         * @param start 起始范围边界
         */
        public WindowFunctionBuilder range(WindowFrameBound start) {
            this.windowFunction.setFrameType(WindowFrameType.RANGE);
            this.windowFunction.setFrameStart(start);
            this.windowFunction.setFrameEnd(null);
            return this;
        }

        /**
         * 完成构建窗口函数
         *
         * @return 构造出的窗口函数实例
         */
        public WindowFunction build() {
            return this.windowFunction;
        }
    }
}
