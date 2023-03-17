package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * update set builder
 */
public class UpdateSetBuilder<ParentBuilder> {
    private ParentBuilder parentBuilder;
    private Table table;
    private UpdateSet set;

    public UpdateSetBuilder(ParentBuilder parentBuilder, Table table, UpdateSet set) {
        Assert.notNull(parentBuilder, "parentBuilder can not be null");
        Assert.notNull(table, "table can not be null");
        Assert.notNull(set, "set can not be null");
        this.parentBuilder = parentBuilder;
        this.table = table;
        this.set = set;
    }

    private void checkEntityTable() {
        if (!(this.table instanceof EntityTable)) {
            throw new IllegalArgumentException("Only EntityTable is supported");
        }
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(String column, CaseWhen caseWhen) {
        //解决java重载问题, 如果第二个参数传入null, 将调用这个方法而不是setField(String field, Object value)
        if (caseWhen == null) {
            return this.setColumn(column, (Object) null);
        }
        this.set.getItems().add(new CaseWhenUpdateColumnItem(this.table, column, caseWhen));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, String column, CaseWhen caseWhen) {
        if (sure) {
            return this.setColumn(column, caseWhen);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(String field, CaseWhen caseWhen) {
        this.checkEntityTable();
        //解决java重载问题, 如果第二个参数传入null, 将调用这个方法而不是setField(String field, Object value)
        if (caseWhen == null) {
            return this.setField(field, (Object) null);
        }
        this.set.getItems().add(new CaseWhenUpdateFieldItem((EntityTable) this.table, field,
                caseWhen));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, String field, CaseWhen caseWhen) {
        if (sure) {
            return this.setField(field, caseWhen);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(boolean sure, String field) {
        if (sure) {
            return this.setFieldToNull(field);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(String field) {
        return this.setField(field, (Object) null);
    }

    public UpdateSetBuilder<ParentBuilder> setField(String field, Object value) {
        this.checkEntityTable();
        this.set.getItems().add(new UpdateFieldItem((EntityTable) this.table, field, value));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, String field, Object value) {
        if (sure) {
            this.setField(field, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnToNull(boolean sure, String column) {
        if (sure) {
            return this.setColumnToNull(column);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnToNull(String column) {
        return this.setColumn(column, (Object) null);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(String column, Object value) {
        this.set.getItems().add(new UpdateColumnItem(this.table, column, value));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, String column, Object value) {
        if (sure) {
            this.setColumn(column, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldSyntax(String field, String syntax) {
        this.checkEntityTable();
        this.set.getItems().add(new SyntaxUpdateFieldItem((EntityTable) this.table, field, syntax));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldSyntax(boolean sure, String field, String syntax) {
        if (sure) {
            return this.setFieldSyntax(field, syntax);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnSyntax(String column, String syntax) {
        this.set.getItems().add(new SyntaxUpdateColumnItem(this.table, column, syntax));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnSyntax(boolean sure, String column, String syntax) {
        if (sure) {
            return this.setColumnSyntax(column, syntax);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(EntityTable table, String field, Object value) {
        this.set.getItems().add(new UpdateFieldItem(table, field, value));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, EntityTable table, String field, Object value) {
        if (sure) {
            return this.setField(table, field, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(Table table, String column, Object value) {
        this.set.getItems().add(new UpdateColumnItem(table, column, value));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, Table table, String column, Object value) {
        if (sure) {
            return this.setColumn(table, column, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldSyntax(EntityTable table, String field, String syntax) {
        this.set.getItems().add(new SyntaxUpdateFieldItem(table, field, syntax));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldSyntax(boolean sure, EntityTable table, String field, String syntax) {
        if (sure) {
            return this.setFieldSyntax(table, field, syntax);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnSyntax(EntityTable table, String field, String syntax) {
        this.set.getItems().add(new SyntaxUpdateColumnItem(table, field, syntax));
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnSyntax(boolean sure, EntityTable table, String field, String syntax) {
        if (sure) {
            return this.setColumnSyntax(table, field, syntax);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFormula(boolean sure, EntityTable table, String field, Formula formula) {
        if (sure) {
            this.set.getItems().add(new FormulaUpdateFieldItem(table, field, formula));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFormula(boolean sure, String field, Formula formula) {
        if (sure) {
            this.checkEntityTable();
            return this.setFieldFormula(sure, (EntityTable) this.table, field, formula);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFormula(String field, Formula formula) {
        this.checkEntityTable();
        return this.setFieldFormula(true, field, formula);
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFunction(boolean sure, EntityTable table, String field, Function function) {
        if (sure) {
            this.set.getItems().add(new FunctionUpdateFieldItem(table, field, function));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFunction(boolean sure, String field, Function function) {
        if (sure) {
            this.checkEntityTable();
            return this.setFieldFunction(sure, (EntityTable) this.table, field, function);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldFunction(String field, Function function) {
        this.checkEntityTable();
        return this.setFieldFunction(true, field, function);
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFormula(boolean sure, Table table, String column, Formula formula) {
        if (sure) {
            this.set.getItems().add(new FormulaUpdateColumnItem(table, column, formula));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFormula(boolean sure, String column, Formula formula) {
        if (sure) {
            return this.setColumnFormula(sure, this.table, column, formula);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFormula(String column, Formula formula) {
        return this.setColumnFormula(true, column, formula);
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFunction(boolean sure, Table table, String column, Function function) {
        if (sure) {
            this.set.getItems().add(new FunctionUpdateColumnItem(table, column, function));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFunction(boolean sure, String column, Function function) {
        if (sure) {
            return this.setColumnFunction(sure, this.table, column, function);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnFunction(String column, Function function) {
        return this.setColumnFunction(true, column, function);
    }

    public UpdateSetBuilder<ParentBuilder> setColumnKeywords(boolean sure, Table table, String column, String keywords) {
        if (sure) {
            this.set.getItems().add(new KeywordsUpdateColumnItem(table, column, keywords));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnKeywords(boolean sure, String column, String keywords) {
        if (sure) {
            return this.setColumnKeywords(sure, this.table, column, keywords);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumnKeywords(String column, String keywords) {
        return this.setColumnKeywords(true, column, keywords);
    }

    public UpdateSetBuilder<ParentBuilder> setFieldKeywords(boolean sure, EntityTable table, String field, String keywords) {
        if (sure) {
            this.set.getItems().add(new KeywordsUpdateFieldItem(table, field, keywords));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldKeywords(boolean sure, String field, String keywords) {
        if (sure) {
            this.checkEntityTable();
            return this.setFieldKeywords(sure, (EntityTable) this.table, field, keywords);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldKeywords(String field, String keywords) {
        return this.setFieldKeywords(true, field, keywords);
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }
}
