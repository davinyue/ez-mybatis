package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * 函数
 */
@Getter
public class Function implements QueryRetNeedAlias {
    /**
     * 表
     */
    private Table table;
    /**
     * 函数名称
     */
    private String funName;
    /**
     * 参数
     */
    private List<FunArg> funArgs;

    private Function() {
    }

    public static FunctionBuilder builder(Table table) {
        return new FunctionBuilder(table);
    }

    /**
     * 函数参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class FunArg {
        /**
         * 添加类属性或者列的时候是否添加去重关键词
         */
        private boolean distinct;
        /**
         * 参数值
         */
        private Operand argValue;

        public FunArg setArgValue(Operand argValue) {
            if (argValue == null) {
                argValue = ObjArg.of(null);
            }
            this.argValue = argValue;
            return this;
        }
    }

    /**
     * 函数构造器
     */
    public static class FunctionBuilder {
        private Function function;

        private FunctionBuilder(Table table) {
            this.function = new Function();
            this.function.table = table;
            this.function.funArgs = new LinkedList<>();
        }

        public FunctionBuilder setFunName(String name) {
            Assert.notEmpty(name, "function name can not be null");
            this.function.funName = name;
            return this;
        }

        private FunctionBuilder addArg(boolean sure, boolean distinct, Operand arg) {
            if (!sure) {
                return this;
            }
            Assert.notNull(arg, "arg can not be null");
            FunArg funArg = new FunArg().setArgValue(arg).setDistinct(distinct);
            this.function.funArgs.add(funArg);
            return this;
        }

        private FunctionBuilder addArg(boolean sure, Operand arg) {
            return this.addArg(sure, false, arg);
        }

        private FunctionBuilder addArg(Operand arg) {
            return this.addArg(true, false, arg);
        }

        private FunctionBuilder addArg(Operand arg, boolean distinct) {
            return this.addArg(true, distinct, arg);
        }

        private FunctionBuilder addColumnArg(boolean sure, boolean distinct, Table table, String column) {
            if (!sure) {
                return this;
            }
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(column, "column can not be null");
            FunArg arg = new FunArg().setArgValue(TableColumn.of(table, column))
                    .setDistinct(distinct);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addColumnArg(boolean sure, Table table, String column) {
            return this.addColumnArg(sure, false, table, column);
        }

        public FunctionBuilder addDistinctColumnArg(boolean sure, Table table, String column) {
            return this.addColumnArg(sure, true, table, column);
        }

        public FunctionBuilder addColumnArg(Table table, String column) {
            return this.addColumnArg(true, table, column);
        }

        public FunctionBuilder addDistinctColumnArg(Table table, String column) {
            return this.addDistinctColumnArg(true, table, column);
        }

        public FunctionBuilder addColumnArg(String column) {
            return this.addColumnArg(this.function.table, column);
        }

        public FunctionBuilder addDistinctColumnArg(String column) {
            return this.addDistinctColumnArg(this.function.table, column);
        }

        public FunctionBuilder addColumnArg(boolean sure, String column) {
            return this.addColumnArg(sure, this.function.table, column);
        }

        public FunctionBuilder addDistinctColumnArg(boolean sure, String column) {
            return this.addDistinctColumnArg(sure, this.function.table, column);
        }

        private FunctionBuilder addFieldArg(boolean sure, boolean distinct, EntityTable table, String field) {
            if (!sure) {
                return this;
            }
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(field, "field can not be null");
            FunArg arg = new FunArg().setArgValue(EntityField.of(table, field))
                    .setDistinct(distinct);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addFieldArg(boolean sure, EntityTable table, String field) {
            return this.addFieldArg(sure, false, table, field);
        }

        public FunctionBuilder addDistinctFieldArg(boolean sure, EntityTable table, String field) {
            return this.addFieldArg(sure, true, table, field);
        }

        public FunctionBuilder addFieldArg(EntityTable table, String field) {
            return this.addFieldArg(true, table, field);
        }

        public FunctionBuilder addDistinctFieldArg(EntityTable table, String field) {
            return this.addDistinctFieldArg(true, table, field);
        }

        protected void checkEntityTable() {
            if (!(this.function.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        public FunctionBuilder addFieldArg(String field) {
            this.checkEntityTable();
            return this.addFieldArg((EntityTable) this.function.table, field);
        }

        public FunctionBuilder addDistinctFieldArg(String field) {
            this.checkEntityTable();
            return this.addDistinctFieldArg((EntityTable) this.function.table, field);
        }

        public FunctionBuilder addFieldArg(boolean sure, String field) {
            this.checkEntityTable();
            return this.addFieldArg(sure, (EntityTable) this.function.table, field);
        }

        public FunctionBuilder addDistinctFieldArg(boolean sure, String field) {
            this.checkEntityTable();
            return this.addDistinctFieldArg(sure, (EntityTable) this.function.table, field);
        }

        public FunctionBuilder addFunArg(boolean sure, Function function) {
            if (!sure) {
                return this;
            }
            Assert.notNull(function, "function can not be null");
            FunArg arg = new FunArg().setArgValue(function);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addFunArg(Function function) {
            return this.addFunArg(true, function);
        }

        public FunctionBuilder addFormulaArg(boolean sure, Formula formula) {
            if (!sure) {
                return this;
            }
            Assert.notNull(formula, "formula can not be null");
            FunArg arg = new FunArg().setArgValue(formula);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addFormulaArg(Formula formula) {
            return this.addFormulaArg(true, formula);
        }

        public FunctionBuilder addValueArg(boolean sure, Object argValue) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgValue(ObjArg.of(argValue));
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addValueArg(Object argValue) {
            return this.addValueArg(true, argValue);
        }

        public FunctionBuilder addKeywordsArg(boolean sure, String keywords) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgValue(Keywords.of(keywords));
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addKeywordsArg(String keywords) {
            return this.addKeywordsArg(true, keywords);
        }

        public FunctionBuilder addCaseWhenArg(boolean sure, CaseWhen caseWhen) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgValue(caseWhen);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addCaseWhenArg(CaseWhen caseWhen) {
            return this.addCaseWhenArg(true, caseWhen);
        }

        public Function build() {
            return this.function;
        }
    }
}
