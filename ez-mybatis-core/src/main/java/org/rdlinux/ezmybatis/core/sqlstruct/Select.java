package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Select implements SqlPart {
    /**
     * 查询
     */
    private EzQuery<?> query;
    /**
     * 是否去重
     */
    private boolean distinct = false;
    ;
    /**
     * 查询项
     */
    private List<SelectItem> selectFields;

    public Select(EzQuery<?> query, List<SelectItem> selectFields) {
        Assert.notNull(query, "query can not be null");
        this.query = query;
        this.selectFields = selectFields;
    }

    public static class EzSelectBuilder<T> {
        private List<SelectItem> selectFields;
        private T target;
        private Table table;
        private Select select;

        public EzSelectBuilder(T target, Select select, Table table) {
            if (select.getSelectFields() == null) {
                select.setSelectFields(new LinkedList<>());
            }
            this.select = select;
            this.selectFields = select.getSelectFields();
            this.target = target;
            this.table = table;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        public T done() {
            return this.target;
        }

        /**
         * 去重
         */
        public EzSelectBuilder<T> distinct() {
            this.select.distinct = true;
            return this;
        }

        /**
         * 不去重
         */
        public EzSelectBuilder<T> notDistinct() {
            this.select.distinct = false;
            return this;
        }

        public EzSelectBuilder<T> addAllTable() {
            this.selectFields.add(new SelectAllItem());
            return this;
        }

        public EzSelectBuilder<T> addAllTable(boolean sure) {
            if (sure) {
                return this.addAllTable();
            }
            return this;
        }

        public EzSelectBuilder<T> addAll() {
            this.selectFields.add(new SelectTableAllItem(this.table));
            return this;
        }

        public EzSelectBuilder<T> addAll(boolean sure) {
            if (sure) {
                return this.addAll();
            }
            return this;
        }


        public EzSelectBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addField(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field, alias));
            return this;
        }


        public EzSelectBuilder<T> addField(boolean sure, String field, String alias) {
            if (sure) {
                return this.addField(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column) {
            this.selectFields.add(new SelectColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column, String alias) {
            this.selectFields.add(new SelectColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumn(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(boolean sure, String field) {
            if (sure) {
                return this.addFieldMax(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMax(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column) {
            this.selectFields.add(new SelectMaxColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(boolean sure, String column) {
            if (sure) {
                return this.addColumnMax(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column, String alias) {
            this.selectFields.add(new SelectMaxColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMax(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(String field) {
            return this.addFieldCount(field, false);
        }

        public EzSelectBuilder<T> addFieldCount(String field, boolean distinct) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, distinct, field));
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field) {
            if (sure) {
                return this.addFieldCount(field, false);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field, boolean distinct) {
            if (sure) {
                return this.addFieldCount(field, distinct);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(String field, String alias) {
            return this.addFieldCount(field, alias, false);
        }

        public EzSelectBuilder<T> addFieldCount(String field, String alias, boolean distinct) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, distinct, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldCount(field, alias, false);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field, String alias, boolean distinct) {
            if (sure) {
                return this.addFieldCount(field, alias, distinct);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column) {
            return this.addColumnCount(column, false);
        }

        public EzSelectBuilder<T> addColumnCount(String column, boolean distinct) {
            this.selectFields.add(new SelectCountColumn(this.table, distinct, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column) {
            if (sure) {
                return this.addColumnCount(column, false);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column, boolean distinct) {
            if (sure) {
                return this.addColumnCount(column, distinct);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column, String alias) {
            return this.addColumnCount(column, alias, false);
        }

        public EzSelectBuilder<T> addColumnCount(String column, String alias, boolean distinct) {
            this.selectFields.add(new SelectCountColumn(this.table, distinct, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnCount(column, alias, false);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column, String alias, boolean distinct) {
            if (sure) {
                return this.addColumnCount(column, alias, distinct);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldMin(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addFieldMin(boolean sure, String field) {
            if (sure) {
                return this.addFieldMin(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldMin(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldMin(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMin(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column) {
            this.selectFields.add(new SelectMinColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(boolean sure, String column) {
            if (sure) {
                return this.addColumnMin(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column, String alias) {
            this.selectFields.add(new SelectMinColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMin(column, alias);
            }
            return this;
        }


        public EzSelectBuilder<T> addFieldAvg(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field));
            return this;
        }


        public EzSelectBuilder<T> addFieldAvg(boolean sure, String field) {
            if (sure) {
                return this.addFieldAvg(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldAvg(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldAvg(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldAvg(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column) {
            this.selectFields.add(new SelectAvgColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(boolean sure, String column) {
            if (sure) {
                return this.addColumnAvg(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column, String alias) {
            this.selectFields.add(new SelectAvgColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnAvg(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectSumField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldSum(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectSumField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(boolean sure, String field) {
            if (sure) {
                return this.addFieldSum(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(String column) {
            this.selectFields.add(new SelectSumColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(boolean sure, String column) {
            if (sure) {
                return this.addColumnSum(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(String column, String alias) {
            this.selectFields.add(new SelectSumColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnSum(column, alias);
            }
            return this;
        }
    }
}
