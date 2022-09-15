package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.group.ColumnGroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.group.FieldGroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.group.GroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

@Getter
@Setter
public class GroupBy implements SqlPart {
    private List<GroupItem> items;

    public GroupBy(List<GroupItem> items) {
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

        /**
         * please use {@link #addField(String)} replace
         */
        @Deprecated
        public GroupBuilder<T> add(String field) {
            return this.addField(field);
        }

        public GroupBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.groupBy.getItems().add(new FieldGroupItem((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String)} replace
         */
        @Deprecated
        public GroupBuilder<T> add(boolean sure, String field) {
            return this.addField(sure, field);
        }

        public GroupBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        public GroupBuilder<T> addColumn(String column) {
            this.groupBy.getItems().add(new ColumnGroupItem(this.table, column));
            return this;
        }

        public GroupBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public T done() {
            return this.target;
        }
    }
}
