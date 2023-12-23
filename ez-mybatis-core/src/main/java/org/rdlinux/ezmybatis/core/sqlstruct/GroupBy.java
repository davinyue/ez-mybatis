package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

@Getter
@Setter
public class GroupBy implements SqlStruct {
    private List<Operand> items;

    public GroupBy(List<Operand> items) {
        this.items = items;
    }

    public static class GroupBuilder<T> {
        private T target;
        private Table table;
        private GroupBy groupBy;

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

        public GroupBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.groupBy.getItems().add(EntityField.of((EntityTable) this.table, field));
            return this;
        }

        public GroupBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        public GroupBuilder<T> addColumn(String column) {
            this.groupBy.getItems().add(TableColumn.of(this.table, column));
            return this;
        }

        public GroupBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public GroupBuilder<T> addAlias(boolean sure, String alias) {
            if (sure) {
                this.groupBy.getItems().add(Alias.of(alias));
            }
            return this;
        }

        public GroupBuilder<T> addAlias(String alias) {
            return this.addAlias(true, alias);
        }

        public GroupBuilder<T> add(boolean sure, Operand operand) {
            if (sure) {
                this.groupBy.getItems().add(operand);
            }
            return this;
        }

        public GroupBuilder<T> add(Operand operand) {
            return this.add(true, operand);
        }

        public T done() {
            return this.target;
        }
    }
}
