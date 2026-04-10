package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.enumeration.OrderType;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 窗口函数，用于实现 SQL 中的 OVER(...) 子句。
 * <p>
 * 与 Function / Formula / CaseWhen 一样，WindowFunction 不绑定任何 Table，是纯粹的无状态结构节点。
 * </p>
 */
@Getter
@Setter
public class WindowFunction implements QueryRetNeedAlias {

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
     * 获取无状态的窗口函数构造器
     *
     * @param function 聚合函数或窗口控制函数 (例如 ROW_NUMBER(), SUM() 等)
     * @return 窗口函数构造器
     */
    public static WindowFunction build(Function function) {
        return new WindowFunctionBuilder(function).build();
    }

    public static WindowFunction build(Function function, Consumer<WindowFunctionBuilder> consumer) {
        WindowFunctionBuilder builder = new WindowFunctionBuilder(function);
        consumer.accept(builder);
        return builder.build();
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
     * <p>
     * 无状态设计，不绑定任何 Table。条件构建时需通过显式传参指定 Operand。
     * </p>
     */
    public static class WindowFunctionBuilder {
        private final WindowFunction windowFunction;

        /**
         * 构造窗口函数的 Builder
         *
         * @param function 内部包裹的具体函数实例
         */
        public WindowFunctionBuilder(Function function) {
            Assert.notNull(function, "function can not be null");
            this.windowFunction = new WindowFunction();
            this.windowFunction.setFunction(function);
            this.windowFunction.setPartitionBy(new LinkedList<>());
            this.windowFunction.setOrderBy(new LinkedList<>());
        }

        // --- PARTITION BY ---

        /**
         * 根据条件添加操作数作为 PARTITION BY 分区元素
         *
         * @param sure    是否满足条件
         * @param operand 操作数（如 EntityField, TableColumn 等）
         */
        public WindowFunctionBuilder partitionBy(boolean sure, Operand operand) {
            if (sure) {
                this.windowFunction.getPartitionBy().add(operand);
            }
            return this;
        }

        /**
         * 添加操作数作为 PARTITION BY 分区元素
         *
         * @param operand 操作数
         */
        public WindowFunctionBuilder partitionBy(Operand operand) {
            return this.partitionBy(true, operand);
        }

        // --- ORDER BY ---

        /**
         * 根据条件添加操作数排序
         *
         * @param sure    是否满足条件
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
         * 根据条件添加操作数升序排序
         *
         * @param sure    是否满足条件
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
         * 设定物理行级边界，仅配置单独的起始点: ROWS start
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
         * 设定逻辑范围边界，仅配置单独的起始范围: RANGE start
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
        private WindowFunction build() {
            return this.windowFunction;
        }
    }
}
