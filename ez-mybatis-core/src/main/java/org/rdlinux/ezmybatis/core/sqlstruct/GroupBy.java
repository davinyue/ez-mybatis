package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * GROUP BY 分组结构
 */
@Getter
@Setter
public class GroupBy implements SqlStruct {
    /**
     * 分组项列表。
     */
    private List<Operand> items;

    /**
     * 使用分组项初始化 GROUP BY 结构。
     *
     * @param items 分组项列表
     */
    private GroupBy(List<Operand> items) {
        this.items = items;
    }

    public static GroupBy build(Consumer<GroupBy.GroupBuilder> gpc) {
        GroupBuilder builder = new GroupBuilder();
        gpc.accept(builder);
        return builder.build();
    }

    public static GroupBy build(List<Operand> items, Consumer<GroupBy.GroupBuilder> gpc) {
        GroupBuilder builder = new GroupBuilder(items);
        gpc.accept(builder);
        return builder.build();
    }

    /**
     * GROUP BY 构造器
     */
    public static class GroupBuilder {
        /**
         * 当前构建中的 GROUP BY 对象。
         */
        private final GroupBy groupBy;

        /**
         * 使用已有分组项初始化构造器。
         *
         * @param items 分组项列表
         */
        private GroupBuilder(List<Operand> items) {
            this.groupBy = new GroupBy(items);
        }

        /**
         * 创建空的分组构造器。
         */
        private GroupBuilder() {
            this.groupBy = new GroupBy(new ArrayList<>());
        }

        /**
         * 根据条件添加通用操作数作为分组项
         *
         * @param sure    是否满足条件
         * @param operand 操作数（如 EntityField, TableColumn, Function 等）
         */
        public GroupBuilder add(boolean sure, Operand operand) {
            if (sure) {
                this.groupBy.getItems().add(operand);
            }
            return this;
        }

        /**
         * 添加通用操作数作为分组项
         *
         * @param operand 操作数
         */
        public GroupBuilder add(Operand operand) {
            return this.add(true, operand);
        }

        /**
         * 结束 GROUP BY 构造
         */
        private GroupBy build() {
            return this.groupBy;
        }
    }
}
