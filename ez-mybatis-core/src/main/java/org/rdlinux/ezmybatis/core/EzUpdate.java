package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.join.JoinType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.*;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzUpdate extends EzParam<Integer> {
    private Update set;
    private List<Join> joins;

    private EzUpdate() {
        super(Integer.class);
        this.set = new Update();
    }

    public static EzUpdateBuilder update(Table table) {
        return new EzUpdateBuilder(table);
    }

    public static class EzUpdateBuilder {
        private EzUpdate update;

        private EzUpdateBuilder(Table table) {
            this.update = new EzUpdate();
            this.update.table = table;
            this.update.from = new From(table);
        }

        public EzUpdateBuilder setColumn(String column, CaseWhen caseWhen) {
            if (caseWhen == null) {
                return this;
            }
            this.update.set.getItems().add(new CaseWhenUpdateColumnItem(this.update.table, column, caseWhen));
            return this;
        }

        public EzUpdateBuilder setColumn(boolean sure, String column, CaseWhen caseWhen) {
            if (sure) {
                return this.setColumn(column, caseWhen);
            }
            return this;
        }

        public EzUpdateBuilder setField(String field, CaseWhen caseWhen) {
            if (caseWhen == null) {
                return this;
            }
            this.update.set.getItems().add(new CaseWhenUpdateFieldItem((EntityTable) this.update.table, field,
                    caseWhen));
            return this;
        }

        public EzUpdateBuilder setField(boolean sure, String field, CaseWhen caseWhen) {
            if (sure) {
                return this.setField(field, caseWhen);
            }
            return this;
        }

        public EzUpdateBuilder setField(String field, Object value) {
            this.update.set.getItems().add(new UpdateFieldItem((EntityTable) this.update.table, field, value));
            return this;
        }

        public EzUpdateBuilder setField(boolean sure, String field, Object value) {
            if (sure) {
                this.setField(field, value);
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

        public EzUpdateBuilder setFieldSyntax(String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateFieldItem((EntityTable) this.update.table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setFieldSyntax(boolean sure, String field, String syntax) {
            if (sure) {
                this.setFieldSyntax(field, syntax);
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

        public EzUpdateBuilder setField(EntityTable table, String field, Object value) {
            this.update.set.getItems().add(new UpdateFieldItem(table, field, value));
            return this;
        }

        public EzUpdateBuilder setField(boolean sure, EntityTable table, String field, Object value) {
            if (sure) {
                this.setField(table, field, value);
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

        public EzUpdateBuilder setFieldSyntax(EntityTable table, String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateFieldItem(table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setFieldSyntax(boolean sure, EntityTable table, String field, String syntax) {
            if (sure) {
                this.setFieldSyntax(table, field, syntax);
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

        public Join.JoinBuilder<EzUpdateBuilder> join(JoinType joinType, Table joinTable) {
            if (this.update.getJoins() == null) {
                this.update.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setJoinType(joinType);
            join.setTable(this.update.table);
            join.setJoinTable(joinTable);
            join.setOnConditions(new LinkedList<>());
            this.update.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where(Table table) {
            if (this.update.where == null) {
                this.update.where = new Where(new LinkedList<>());
            }
            return new Where.WhereBuilder<>(this, this.update.where, table);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where() {
            return this.where(this.update.table);
        }

        public EzUpdate build() {
            return this.update;
        }
    }
}
