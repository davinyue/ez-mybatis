package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
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

    public UpdateSetBuilder<ParentBuilder> setField(EntityTable table, String field, Object value) {
        if (value == null) {
            this.set.getItems().add(new UpdateFieldItem(table, field, null));
        } else if (value instanceof CaseWhen) {
            this.set.getItems().add(new CaseWhenUpdateFieldItem(table, field, (CaseWhen) value));
        } else if (value instanceof Formula) {
            this.set.getItems().add(new FormulaUpdateFieldItem(table, field, (Formula) value));
        } else if (value instanceof Function) {
            this.set.getItems().add(new FunctionUpdateFieldItem(table, field, (Function) value));
        } else if (value instanceof Keywords) {
            this.set.getItems().add(new KeywordsUpdateFieldItem(table, field, (Keywords) value));
        } else {
            this.set.getItems().add(new UpdateFieldItem(table, field, value));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(String field) {
        return this.setField(field, null);
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(boolean sure, String field) {
        if (sure) {
            return this.setFieldToNull(field);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(String field, Object value) {
        this.checkEntityTable();
        return this.setField((EntityTable) this.table, field, value);
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, String field, Object value) {
        if (sure) {
            this.setField(field, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, EntityTable table, String field, Object value) {
        if (sure) {
            return this.setField(table, field, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(Table table, String column, Object value) {
        if (value == null) {
            this.set.getItems().add(new UpdateColumnItem(table, column, null));
        } else if (value instanceof CaseWhen) {
            this.set.getItems().add(new CaseWhenUpdateColumnItem(table, column, (CaseWhen) value));
        } else if (value instanceof Formula) {
            this.set.getItems().add(new FormulaUpdateColumnItem(table, column, (Formula) value));
        } else if (value instanceof Function) {
            this.set.getItems().add(new FunctionUpdateColumnItem(table, column, (Function) value));
        } else if (value instanceof Keywords) {
            this.set.getItems().add(new KeywordsUpdateColumnItem(table, column, (Keywords) value));
        } else {
            this.set.getItems().add(new UpdateColumnItem(table, column, value));
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
        return this.setColumn(column, null);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(String column, Object value) {
        return this.setColumn(this.table, column, value);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, String column, Object value) {
        if (sure) {
            this.setColumn(column, value);
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, Table table, String column, Object value) {
        if (sure) {
            return this.setColumn(table, column, value);
        }
        return this;
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }
}
