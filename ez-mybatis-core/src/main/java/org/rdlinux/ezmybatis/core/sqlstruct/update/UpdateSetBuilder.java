package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
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

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, EntityTable table, String field, Operand value) {
        if (sure) {
            this.set.getItems().add(new UpdateFieldItem(table, field, value));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setField(EntityTable table, String field, Operand value) {
        return this.setField(true, table, field, value);
    }


    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, String field, Operand value) {
        this.checkEntityTable();
        return this.setField(sure, (EntityTable) this.table, field, value);
    }

    public UpdateSetBuilder<ParentBuilder> setField(String field, Operand value) {
        this.checkEntityTable();
        return this.setField(true, (EntityTable) this.table, field, value);
    }


    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, EntityTable table, String field, Object value) {
        return this.setField(sure, table, field, Operand.objToOperand(value));
    }

    public UpdateSetBuilder<ParentBuilder> setField(EntityTable table, String field, Object value) {
        return this.setField(true, table, field, value);
    }

    public UpdateSetBuilder<ParentBuilder> setField(boolean sure, String field, Object value) {
        this.checkEntityTable();
        return this.setField(sure, (EntityTable) this.table, field, value);
    }

    public UpdateSetBuilder<ParentBuilder> setField(String field, Object value) {
        this.checkEntityTable();
        return this.setField(true, (EntityTable) this.table, field, value);
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(String field) {
        this.checkEntityTable();
        return this.setField(true, (EntityTable) this.table, field, null);
    }

    public UpdateSetBuilder<ParentBuilder> setFieldToNull(boolean sure, String field) {
        this.checkEntityTable();
        return this.setField(sure, (EntityTable) this.table, field, null);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, Table table, String column, Operand value) {
        if (sure) {
            this.set.getItems().add(new UpdateColumnItem(table, column, value));
        }
        return this;
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(Table table, String column, Operand value) {
        return this.setColumn(true, table, column, value);
    }


    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, String column, Operand value) {

        return this.setColumn(sure, this.table, column, value);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(String column, Operand value) {

        return this.setColumn(true, this.table, column, value);
    }


    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, Table table, String column, Object value) {
        return this.setColumn(sure, table, column, Operand.objToOperand(value));
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(Table table, String column, Object value) {
        return this.setColumn(true, table, column, value);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(boolean sure, String column, Object value) {

        return this.setColumn(sure, this.table, column, value);
    }

    public UpdateSetBuilder<ParentBuilder> setColumn(String column, Object value) {

        return this.setColumn(true, this.table, column, value);
    }

    public UpdateSetBuilder<ParentBuilder> setColumnToNull(String column) {

        return this.setColumn(true, this.table, column, null);
    }

    public UpdateSetBuilder<ParentBuilder> setColumnToNull(boolean sure, String column) {

        return this.setColumn(sure, this.table, column, null);
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }
}
