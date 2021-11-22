package org.rdlinux.ezmybatis.core.sqlpart;

import java.util.List;

public class EzGroup {
    private List<GroupItem> items;

    public EzGroup(List<GroupItem> items) {
        this.items = items;
    }

    public List<GroupItem> getItems() {
        return this.items;
    }

    public void setItems(List<GroupItem> items) {
        this.items = items;
    }


    public static class GroupItem {
        private EzTable table;
        private String field;

        public GroupItem(EzTable table, String field) {
            this.table = table;
            this.field = field;
        }

        public EzTable getTable() {
            return this.table;
        }

        public String getField() {
            return this.field;
        }
    }
}
