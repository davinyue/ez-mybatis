package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

@Getter
@Setter
public class GroupBy implements SqlStruct {
    private List<GroupItem> items;

    public GroupBy(List<GroupItem> items) {
        this.items = items;
    }

    /**
     * 分组项
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class GroupItem implements SqlStruct {
        /**
         * 表
         */
        private Table table;
        /**
         * 值类型
         */
        private ArgType argType;
        /**
         * 值
         */
        private Object value;
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
            this.groupBy.getItems().add(new GroupItem().setTable(this.table).setValue(field)
                    .setArgType(ArgType.FILED));
            return this;
        }

        public GroupBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        public GroupBuilder<T> addColumn(String column) {
            this.groupBy.getItems().add(new GroupItem().setTable(this.table).setValue(column)
                    .setArgType(ArgType.COLUMN));
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
                this.groupBy.getItems().add(new GroupItem().setValue(alias).setArgType(ArgType.ALIAS));
            }
            return this;
        }

        public GroupBuilder<T> addAlias(String alias) {
            return this.addAlias(true, alias);
        }

        public GroupBuilder<T> addFormula(boolean sure, Formula formula) {
            if (sure) {
                this.groupBy.getItems().add(new GroupItem().setValue(formula).setArgType(ArgType.FORMULA));
            }
            return this;
        }

        public GroupBuilder<T> addFormula(Formula formula) {
            return this.addFormula(true, formula);
        }

        public GroupBuilder<T> addFunc(boolean sure, Function function) {
            if (sure) {
                this.groupBy.getItems().add(new GroupItem().setValue(function).setArgType(ArgType.FUNC));
            }
            return this;
        }

        public GroupBuilder<T> addFunc(Function function) {
            return this.addFunc(true, function);
        }

        public GroupBuilder<T> addCaseWhen(boolean sure, CaseWhen caseWhen) {
            if (sure) {
                this.groupBy.getItems().add(new GroupItem().setValue(caseWhen).setArgType(ArgType.CASE_WHEN));
            }
            return this;
        }

        public GroupBuilder<T> addCaseWhen(CaseWhen caseWhen) {
            return this.addCaseWhen(true, caseWhen);
        }

        public T done() {
            return this.target;
        }
    }
}
