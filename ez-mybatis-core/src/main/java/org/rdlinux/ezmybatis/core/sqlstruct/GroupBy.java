package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

/**
 * GROUP BY 分组结构
 */
@Getter
@Setter
public class GroupBy implements SqlStruct {
    private List<Operand> items;

    public GroupBy(List<Operand> items) {
        this.items = items;
    }

    /**
     * GROUP BY 构造器
     */
    public static class GroupBuilder<T> {
        private final T target;
        private final Table table;
        private final GroupBy groupBy;

        public GroupBuilder(T target, GroupBy groupBy, Table table) {
            this.target = target;
            this.groupBy = groupBy;
            this.table = table;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * 添加当前实体表的属性作为分组字段（保留作高频语法糖）
         *
         * @param field 实体属性名
         */
        public GroupBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.groupBy.getItems().add(EntityField.of((EntityTable) this.table, field));
            return this;
        }

        /**
         * 根据条件添加当前实体表的属性作为分组字段
         *
         * @param sure  是否满足条件
         * @param field 实体属性名
         */
        public GroupBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        /**
         * 根据条件添加通用操作数作为分组项
         *
         * @param sure    是否满足条件
         * @param operand 操作数（如 EntityField, TableColumn, Function 等）
         */
        public GroupBuilder<T> add(boolean sure, Operand operand) {
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
        public GroupBuilder<T> add(Operand operand) {
            return this.add(true, operand);
        }

        /**
         * 结束 GROUP BY 构造
         */
        public T done() {
            return this.target;
        }
    }
}
