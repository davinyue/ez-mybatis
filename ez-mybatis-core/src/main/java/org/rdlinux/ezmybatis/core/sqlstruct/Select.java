package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Select implements SqlStruct {
    /**
     * 查询
     */
    private EzQuery<?> query;
    /**
     * sql提示
     */
    private SqlHint sqlHint;
    /**
     * 是否去重
     */
    private boolean distinct = false;
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
        private final List<SelectItem> selectFields;
        private final T target;
        private final Table table;
        private final Select select;

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

        /**
         * @param excludeField 排除的查询项, 只支持entityTable
         */
        public EzSelectBuilder<T> addAll(String... excludeField) {
            this.selectFields.add(new SelectTableAllItem(this.table, excludeField));
            return this;
        }

        /**
         * @param excludeField 排除的查询项, 只支持entityTable
         */
        public EzSelectBuilder<T> addAll(boolean sure, String... excludeField) {
            if (sure) {
                return this.addAll(excludeField);
            }
            return this;
        }


        public EzSelectBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(EntityField.of((EntityTable) this.table, field), null));
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
            this.selectFields.add(new SelectOperand(EntityField.of((EntityTable) this.table, field), alias));
            return this;
        }


        public EzSelectBuilder<T> addField(boolean sure, String field, String alias) {
            if (sure) {
                return this.addField(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column) {
            this.selectFields.add(new SelectOperand(TableColumn.of(this.table, column), null));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column, String alias) {
            this.selectFields.add(new SelectOperand(TableColumn.of(this.table, column), alias));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumn(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("MAX")
                    .addFieldArg(field).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldMax(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMax(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column, String alias) {
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("MAX")
                    .addColumnArg(column).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMax(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldCount(String field, String alias) {
            return this.addFieldCount(field, alias, false);
        }

        public EzSelectBuilder<T> addFieldCount(String field, String alias, boolean distinct) {
            this.checkEntityTable();
            if (distinct) {
                this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("COUNT")
                        .addDistinctFieldArg(field).build(), alias));
            } else {
                this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("COUNT")
                        .addFieldArg(field).build(), alias));
            }
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

        public EzSelectBuilder<T> addColumnCount(String column, String alias) {
            return this.addColumnCount(column, alias, false);
        }

        public EzSelectBuilder<T> addColumnCount(String column, String alias, boolean distinct) {
            if (distinct) {
                this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("COUNT")
                        .addDistinctColumnArg(column).build(), alias));
            } else {
                this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("COUNT")
                        .addColumnArg(column).build(), alias));
            }
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

        public EzSelectBuilder<T> addFieldMin(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("MIN")
                    .addFieldArg(field).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldMin(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMin(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column, String alias) {
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("MIN")
                    .addColumnArg(column).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMin(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldAvg(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("AVG")
                    .addFieldArg(field).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldAvg(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldAvg(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column, String alias) {
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("AVG")
                    .addColumnArg(column).build(), alias));
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
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("SUM")
                    .addFieldArg(field).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldSum(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(String column, String alias) {
            this.selectFields.add(new SelectOperand(Function.builder(this.table).setFunName("SUM")
                    .addColumnArg(column).build(), alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnSum(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> add(boolean sure, QueryRetOperand operand, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(operand, alias));
            }
            return this;
        }

        public EzSelectBuilder<T> add(boolean sure, QueryRetOperand operand) {
            return this.add(sure, operand, null);
        }

        public EzSelectBuilder<T> add(QueryRetOperand operand, String alias) {
            return this.add(true, operand, alias);
        }

        public EzSelectBuilder<T> add(QueryRetOperand operand) {
            return this.add(true, operand, null);
        }

        public EzSelectBuilder<T> addKeywords(boolean sure, String keywords, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(Keywords.of(keywords), alias));
            }
            return this;
        }

        public EzSelectBuilder<T> addKeywords(boolean sure, String keywords) {
            return this.addKeywords(sure, keywords, null);
        }

        public EzSelectBuilder<T> addKeywords(String keywords, String alias) {
            return this.addKeywords(true, keywords, alias);
        }

        public EzSelectBuilder<T> addKeywords(String keywords) {
            return this.addKeywords(true, keywords, null);
        }

        public EzSelectBuilder<T> addFunc(boolean sure, Function function, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(function, alias));
            }
            return this;
        }

        public EzSelectBuilder<T> addFunc(Function function, String alias) {
            return this.addFunc(true, function, alias);
        }

        public EzSelectBuilder<T> addFormula(boolean sure, Formula formula, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(formula, alias));
            }
            return this;
        }

        public EzSelectBuilder<T> addFormula(Formula formula, String alias) {
            return this.addFormula(true, formula, alias);
        }

        public EzSelectBuilder<T> addCaseWhen(boolean sure, CaseWhen caseWhen, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(caseWhen, alias));
            }
            return this;
        }

        public EzSelectBuilder<T> addCaseWhen(CaseWhen caseWhen, String alias) {
            return this.addCaseWhen(true, caseWhen, alias);
        }

        public EzSelectBuilder<T> addValue(boolean sure, Object value, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(ObjArg.of(value), alias));
            }
            return this;
        }

        public EzSelectBuilder<T> addValue(Object value, String alias) {
            return this.addValue(true, value, alias);
        }

        /**
         * 指定sql提示
         */
        public EzSelectBuilder<T> withHint(String hint) {
            return this.withHint(true, hint);
        }

        /**
         * 指定sql提示
         */
        public EzSelectBuilder<T> withHint(boolean sure, String hint) {
            if (sure && StringUtils.isNotEmpty(hint)) {
                this.select.sqlHint = new SqlHint(hint);
            }
            return this;
        }
    }
}
