package ink.dvc.ezmybatis.core;

import ink.dvc.ezmybatis.core.sqlstruct.From;
import ink.dvc.ezmybatis.core.sqlstruct.Join;
import ink.dvc.ezmybatis.core.sqlstruct.UpdateSet;
import ink.dvc.ezmybatis.core.sqlstruct.Where;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.sqlstruct.update.SyntaxUpdateColumnItem;
import ink.dvc.ezmybatis.core.sqlstruct.update.SyntaxUpdateFieldItem;
import ink.dvc.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import ink.dvc.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzUpdate extends EzParam<Integer> {
    private UpdateSet set;
    private List<Join> joins;

    private EzUpdate() {
        super(Integer.class);
        this.set = new UpdateSet();
    }

    public static EzUpdateBuilder update(EntityTable table) {
        return new EzUpdateBuilder(table);
    }

    public static class EzUpdateBuilder {
        private EzUpdate update;

        private EzUpdateBuilder(EntityTable table) {
            this.update = new EzUpdate();
            this.update.table = table;
            this.update.from = new From(table);
        }

        public EzUpdateBuilder set(String field, Object value) {
            this.update.set.getItems().add(new UpdateFieldItem((EntityTable) this.update.table, field, value));
            return this;
        }

        public EzUpdateBuilder set(boolean sure, String field, Object value) {
            if (sure) {
                this.set(field, value);
            }
            return this;
        }

        public EzUpdateBuilder setColumn(String column, Object value) {
            this.update.set.getItems().add(new UpdateColumnItem(this.update.table, column, value));
            return this;
        }

        public EzUpdateBuilder setColumn(boolean sure, String column, Object value) {
            if (sure) {
                this.setColumn(column, value);
            }
            return this;
        }

        public EzUpdateBuilder setSyntax(String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateFieldItem((EntityTable) this.update.table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setSyntax(boolean sure, String field, String syntax) {
            if (sure) {
                this.setSyntax(field, syntax);
            }
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(String column, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateColumnItem(this.update.table, column, syntax));
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(boolean sure, String column, String syntax) {
            if (sure) {
                this.setColumnSyntax(column, syntax);
            }
            return this;
        }

        public EzUpdateBuilder set(EntityTable table, String field, Object value) {
            this.update.set.getItems().add(new UpdateFieldItem(table, field, value));
            return this;
        }

        public EzUpdateBuilder set(boolean sure, EntityTable table, String field, Object value) {
            if (sure) {
                this.set(table, field, value);
            }
            return this;
        }

        public EzUpdateBuilder setColumn(Table table, String column, Object value) {
            this.update.set.getItems().add(new UpdateColumnItem(table, column, value));
            return this;
        }

        public EzUpdateBuilder setColumn(boolean sure, Table table, String column, Object value) {
            if (sure) {
                this.setColumn(table, column, value);
            }
            return this;
        }

        public EzUpdateBuilder setSyntax(EntityTable table, String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateFieldItem(table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setSyntax(boolean sure, EntityTable table, String field, String syntax) {
            if (sure) {
                this.setSyntax(table, field, syntax);
            }
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(EntityTable table, String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateColumnItem(table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(boolean sure, EntityTable table, String field, String syntax) {
            if (sure) {
                this.setColumnSyntax(table, field, syntax);
            }
            return this;
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(EntityTable joinTable) {
            if (this.update.getJoins() == null) {
                this.update.joins = new LinkedList<>();
            }
            Join join = new Join();
            this.update.joins.add(join);
            return new Join.JoinBuilder<>(this, join, (EntityTable) this.update.table, joinTable);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where() {
            Where where = new Where(new LinkedList<>());
            this.update.where = where;
            return new Where.WhereBuilder<>(this, where, (EntityTable) this.update.table);
        }

        public EzUpdate build() {
            return this.update;
        }
    }
}
