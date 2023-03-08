package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
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

        private void checkEntityTable() {
            if (!(this.update.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        public EzUpdateBuilder setColumn(String column, CaseWhen caseWhen) {
            //解决java重载问题, 如果第二个参数传入null, 将调用这个方法而不是setField(String field, Object value)
            if (caseWhen == null) {
                return this.setColumn(column, (Object) null);
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
            this.checkEntityTable();
            //解决java重载问题, 如果第二个参数传入null, 将调用这个方法而不是setField(String field, Object value)
            if (caseWhen == null) {
                return this.setField(field, (Object) null);
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

        public EzUpdateBuilder setFieldToNull(boolean sure, String field) {
            if (sure) {
                return this.setFieldToNull(field);
            }
            return this;
        }

        public EzUpdateBuilder setFieldToNull(String field) {
            return this.setField(field, (Object) null);
        }

        public EzUpdateBuilder setField(String field, Object value) {
            this.checkEntityTable();
            this.update.set.getItems().add(new UpdateFieldItem((EntityTable) this.update.table, field, value));
            return this;
        }

        public EzUpdateBuilder setField(boolean sure, String field, Object value) {
            if (sure) {
                this.setField(field, value);
            }
            return this;
        }

        public EzUpdateBuilder setColumnToNull(boolean sure, String column) {
            if (sure) {
                return this.setColumnToNull(column);
            }
            return this;
        }

        public EzUpdateBuilder setColumnToNull(String column) {
            return this.setColumn(column, (Object) null);
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
            this.checkEntityTable();
            this.update.set.getItems().add(new SyntaxUpdateFieldItem((EntityTable) this.update.table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setFieldSyntax(boolean sure, String field, String syntax) {
            if (sure) {
                return this.setFieldSyntax(field, syntax);
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
                return this.setField(table, field, value);
            }
            return this;
        }

        public EzUpdateBuilder setColumn(Table table, String column, Object value) {
            this.update.set.getItems().add(new UpdateColumnItem(table, column, value));
            return this;
        }

        public EzUpdateBuilder setColumn(boolean sure, Table table, String column, Object value) {
            if (sure) {
                return this.setColumn(table, column, value);
            }
            return this;
        }

        public EzUpdateBuilder setFieldSyntax(EntityTable table, String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateFieldItem(table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setFieldSyntax(boolean sure, EntityTable table, String field, String syntax) {
            if (sure) {
                return this.setFieldSyntax(table, field, syntax);
            }
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(EntityTable table, String field, String syntax) {
            this.update.set.getItems().add(new SyntaxUpdateColumnItem(table, field, syntax));
            return this;
        }

        public EzUpdateBuilder setColumnSyntax(boolean sure, EntityTable table, String field, String syntax) {
            if (sure) {
                return this.setColumnSyntax(table, field, syntax);
            }
            return this;
        }

        public EzUpdateBuilder setFieldFormula(boolean sure, EntityTable table, String field, Formula formula) {
            if (sure) {
                this.update.set.getItems().add(new FormulaUpdateFieldItem(table, field, formula));
            }
            return this;
        }

        public EzUpdateBuilder setFieldFormula(boolean sure, String field, Formula formula) {
            if (sure) {
                this.checkEntityTable();
                return this.setFieldFormula(sure, (EntityTable) this.update.table, field, formula);
            }
            return this;
        }

        public EzUpdateBuilder setFieldFormula(String field, Formula formula) {
            this.checkEntityTable();
            return this.setFieldFormula(true, field, formula);
        }

        public EzUpdateBuilder setFieldFunction(boolean sure, EntityTable table, String field, Function function) {
            if (sure) {
                this.update.set.getItems().add(new FunctionUpdateFieldItem(table, field, function));
            }
            return this;
        }

        public EzUpdateBuilder setFieldFunction(boolean sure, String field, Function function) {
            if (sure) {
                this.checkEntityTable();
                return this.setFieldFunction(sure, (EntityTable) this.update.table, field, function);
            }
            return this;
        }

        public EzUpdateBuilder setFieldFunction(String field, Function function) {
            this.checkEntityTable();
            return this.setFieldFunction(true, field, function);
        }

        public EzUpdateBuilder setColumnFormula(boolean sure, Table table, String column, Formula formula) {
            if (sure) {
                this.update.set.getItems().add(new FormulaUpdateColumnItem(table, column, formula));
            }
            return this;
        }

        public EzUpdateBuilder setColumnFormula(boolean sure, String column, Formula formula) {
            if (sure) {
                return this.setColumnFormula(sure, this.update.table, column, formula);
            }
            return this;
        }

        public EzUpdateBuilder setColumnFormula(String column, Formula formula) {
            return this.setColumnFormula(true, column, formula);
        }

        public EzUpdateBuilder setColumnFunction(boolean sure, Table table, String column, Function function) {
            if (sure) {
                this.update.set.getItems().add(new FunctionUpdateColumnItem(table, column, function));
            }
            return this;
        }

        public EzUpdateBuilder setColumnFunction(boolean sure, String column, Function function) {
            if (sure) {
                return this.setColumnFunction(sure, this.update.table, column, function);
            }
            return this;
        }

        public EzUpdateBuilder setColumnFunction(String column, Function function) {
            return this.setColumnFunction(true, column, function);
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
