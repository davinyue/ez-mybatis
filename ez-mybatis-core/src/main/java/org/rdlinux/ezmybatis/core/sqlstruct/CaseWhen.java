package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * CaseWhen条件
 */
@Getter
@Setter
public class CaseWhen implements QueryRetNeedAlias, Operand {
    /**
     * 表
     */
    private Table table;
    /**
     * CaseWhen条件数据
     */
    private List<CaseWhenData> caseWhenData;
    /**
     * CaseWhen条件else数据
     */
    private CaseWhenData els;

    private CaseWhen(Table table) {
        this.table = table;
    }

    /**
     * 获取构造器
     */
    public static CaseWhenBuilder builder(Table table) {
        return new CaseWhenBuilder(table);
    }

    /**
     * CaseWhen条件数据
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class CaseWhenData {
        /**
         * 条件
         */
        private List<Condition> conditions;
        /**
         * 表
         */
        private Table table;
        /**
         * 值
         */
        private Operand value;

        public CaseWhenData setValue(Operand value) {
            if (value == null) {
                value = ObjArg.of(value);
            }
            this.value = value;
            return this;
        }

        /**
         * CaseWhen条件数据构造器
         */
        public static class CaseWhenDataBuilder extends ConditionBuilder<CaseWhenBuilder, CaseWhenDataBuilder> {
            private CaseWhenData caseWhenData;

            public CaseWhenDataBuilder(Table table, CaseWhenBuilder caseWhenBuilder, CaseWhenData caseWhenData) {
                super(caseWhenBuilder, caseWhenData.getConditions(), table, table);
                this.sonBuilder = this;
                this.caseWhenData = caseWhenData;
            }

            /**
             * 条件匹配时的值
             */
            public CaseWhenBuilder then(Object value) {
                this.caseWhenData.setValue(ObjArg.of(value));
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值
             */
            public CaseWhenBuilder thenKeywords(String keywords) {
                Assert.notEmpty(keywords, keywords);
                this.caseWhenData.setValue(Keywords.of(keywords));
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值, 返回列
             */
            public CaseWhenBuilder thenColumn(Table table, String column) {
                Assert.notNull(table, "table can not be null");
                Assert.notEmpty(column, "column can not be null");
                this.caseWhenData.setTable(table).setValue(TableColumn.of(table, column));
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值, 返回列
             */
            public CaseWhenBuilder thenColumn(String column) {
                return this.thenColumn(this.table, column);
            }

            /**
             * 条件匹配时的值, 返回实体属性对应列
             */
            public CaseWhenBuilder thenField(EntityTable table, String field) {
                Assert.notNull(table, "table can not be null");
                Assert.notEmpty(field, "field can not be null");
                this.caseWhenData.setTable(table).setValue(EntityField.of(table, field));
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值, 返回实体属性对应列
             */
            public CaseWhenBuilder thenField(String field) {
                this.checkEntityTable();
                return this.thenField((EntityTable) this.table, field);
            }

            /**
             * 条件匹配时的值, 返回函数
             */
            public CaseWhenBuilder thenFunc(Function function) {
                Assert.notNull(function, "function can not be null");
                this.caseWhenData.setValue(function);
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值, 返回计算公式
             */
            public CaseWhenBuilder thenFormula(Formula formula) {
                Assert.notNull(formula, "formula can not be null");
                this.caseWhenData.setValue(formula);
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值, 返回case when
             */
            public CaseWhenBuilder thenCaseWhen(CaseWhen caseWhen) {
                Assert.notNull(caseWhen, "caseWhen can not be null");
                this.caseWhenData.setValue(caseWhen);
                return this.parentBuilder;
            }

            /**
             * 条件匹配时的值
             */
            public CaseWhenBuilder then(Operand value) {
                this.caseWhenData.setValue(value);
                return this.parentBuilder;
            }
        }

    }

    /**
     * CaseWhen构造器
     */
    public static class CaseWhenBuilder {
        protected Table table;
        protected CaseWhen caseWhen;

        private CaseWhenBuilder(Table table) {
            this.table = table;
            this.caseWhen = new CaseWhen(table);
        }

        /**
         * 添加case when条件
         */
        public CaseWhenData.CaseWhenDataBuilder when() {
            if (this.caseWhen.getCaseWhenData() == null) {
                this.caseWhen.setCaseWhenData(new LinkedList<>());
            }
            CaseWhenData caseWhenData = new CaseWhenData();
            caseWhenData.setConditions(new LinkedList<>());
            this.caseWhen.getCaseWhenData().add(caseWhenData);
            return new CaseWhenData.CaseWhenDataBuilder(this.table, this, caseWhenData);
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen els(Object value) {
            this.caseWhen.setEls(new CaseWhenData().setValue(ObjArg.of(value)));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsKeywords(String keywords) {
            this.caseWhen.setEls(new CaseWhenData().setValue(Keywords.of(keywords)));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsColumn(Table table, String column) {
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(column, "column can not be null");
            this.caseWhen.setEls(new CaseWhenData().setTable(table).setValue(TableColumn.of(table, column)));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsColumn(String column) {
            return this.elsColumn(this.table, column);
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsField(EntityTable table, String field) {
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(field, "field can not be null");
            this.caseWhen.setEls(new CaseWhenData().setTable(table).setValue(EntityField.of(table, field)));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsField(String field) {
            this.checkEntityTable();
            return this.elsField((EntityTable) this.table, field);
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsFunc(Function function) {
            Assert.notNull(function, "function can not be null");
            this.caseWhen.setEls(new CaseWhenData().setValue(function));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsFormula(Formula formula) {
            Assert.notNull(formula, "formula can not be null");
            this.caseWhen.setEls(new CaseWhenData().setValue(formula));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen elsCaseWhen(CaseWhen caseWhen) {
            Assert.notNull(caseWhen, "caseWhen can not be null");
            this.caseWhen.setEls(new CaseWhenData().setValue(caseWhen));
            return this.caseWhen;
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen els(Operand value) {
            this.caseWhen.setEls(new CaseWhenData().setValue(value));
            return this.caseWhen;
        }


        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * 构造结束
         */
        public CaseWhen build() {
            return this.caseWhen;
        }
    }
}
