package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

/**
 * 计算公式
 */
@Getter
public class Formula implements SqlStruct {
    /**
     * 表
     */
    private Table table;
    /**
     * 公式要素
     */
    private List<FormulaElement> elements;

    private Formula() {
    }

    public static FormulaEleBuilder<FormulaBuilder> builder(Table table) {
        List<FormulaElement> elements = new LinkedList<>();
        FormulaBuilder formulaBuilder = new FormulaBuilder(table, elements);
        return new FormulaEleBuilder<>(formulaBuilder, table, elements);
    }

    public static class FormulaBuilder {
        private Formula formula;

        public FormulaBuilder(Table table, List<FormulaElement> elements) {
            this.formula = new Formula();
            this.formula.table = table;
            this.formula.elements = elements;
        }

        public Formula build() {
            return this.formula;
        }
    }


    /**
     * @param <ParentBuilder> 上级构造器, 调用.done时将返回上级构造器
     */
    public static class FormulaEleBuilder<ParentBuilder> {
        protected ParentBuilder parentBuilder;
        protected List<FormulaElement> elements;
        protected Table table;


        public FormulaEleBuilder(ParentBuilder parentBuilder, Table table, List<FormulaElement> elements) {
            this.parentBuilder = parentBuilder;
            this.table = table;
            this.elements = elements;
        }

        public ParentBuilder done() {
            return this.parentBuilder;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * 指定运算符并以列构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withColumn(FormulaOperator operator, Table table, String column) {
            FormulaElement element = new ColumnFormulaElement(operator, table, column);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以列开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withColumn(Table table, String column) {
            return this.withColumn(FormulaOperator.EMPTY, table, column);
        }

        /**
         * 指定运算符并以列构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withColumn(FormulaOperator operator, String column) {
            return this.withColumn(operator, this.table, column);
        }

        /**
         * 以列开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withColumn(String column) {
            return this.withColumn(FormulaOperator.EMPTY, this.table, column);
        }

        /**
         * 指定运算符并以实体属性构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withField(FormulaOperator operator, EntityTable table, String field) {
            FormulaElement element = new FieldFormulaElement(operator, table, field);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以实体属性开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withField(EntityTable table, String field) {
            return this.withField(FormulaOperator.EMPTY, table, field);
        }

        /**
         * 指定运算符并以实体属性构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withField(FormulaOperator operator, String field) {
            this.checkEntityTable();
            return this.withField(operator, (EntityTable) this.table, field);
        }

        /**
         * 以实体属性开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withField(String field) {
            this.checkEntityTable();
            return this.withField(FormulaOperator.EMPTY, (EntityTable) this.table, field);
        }

        /**
         * 指定运算符并以函数构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withFun(FormulaOperator operator, Function fun) {
            FormulaElement element = new FunFormulaElement(operator, fun);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以函数开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withFun(Function fun) {
            return this.withFun(FormulaOperator.EMPTY, fun);
        }

        /**
         * 指定运算符并以自定义值构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withValue(FormulaOperator operator, Object ojb) {
            FormulaElement element = new ValueFormulaElement(operator, ojb);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以自定义值开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withValue(Object ojb) {
            return this.withValue(FormulaOperator.EMPTY, ojb);
        }

        /**
         * 指定运算符并以计算公式构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withFormula(FormulaOperator operator, Formula formula) {
            FormulaElement element = new FormulaFormulaElement(operator, formula);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以计算公式开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withFormula(Formula formula) {
            return this.withFormula(FormulaOperator.EMPTY, formula);
        }

        /**
         * 指定运算符并以关键词构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withKeywords(FormulaOperator operator, String keywords) {
            FormulaElement element = new KeywordsFormulaElement(operator, keywords);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以关键词开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withKeywords(String keywords) {
            return this.withKeywords(FormulaOperator.EMPTY, keywords);
        }

        /**
         * 指定运算符并以()构建计算公式
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> withGroup(FormulaOperator operator) {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(operator, elements);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return new FormulaEleBuilder<>(this, this.table, elements);
        }

        /**
         * 以()开始, 构建计算公式
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> withGroup() {
            return this.withGroup(FormulaOperator.EMPTY);
        }

        /**
         * 加列
         */
        public FormulaEleBuilder<ParentBuilder> addColumn(Table table, String column) {
            FormulaElement element = new ColumnFormulaElement(FormulaOperator.ADD, table, column);
            this.elements.add(element);
            return this;
        }

        /**
         * 加列
         */
        public FormulaEleBuilder<ParentBuilder> addColumn(String column) {
            return this.addColumn(this.table, column);
        }

        /**
         * 加实体属性
         */
        public FormulaEleBuilder<ParentBuilder> addField(EntityTable table, String field) {
            FormulaElement element = new FieldFormulaElement(FormulaOperator.ADD, table, field);
            this.elements.add(element);
            return this;
        }

        /**
         * 加实体属性
         */
        public FormulaEleBuilder<ParentBuilder> addField(String field) {
            this.checkEntityTable();
            return this.addField((EntityTable) this.table, field);
        }

        /**
         * 加函数
         */
        public FormulaEleBuilder<ParentBuilder> addFun(Function fun) {
            FormulaElement element = new FunFormulaElement(FormulaOperator.ADD, fun);
            this.elements.add(element);
            return this;
        }

        /**
         * 加自定义值
         */
        public FormulaEleBuilder<ParentBuilder> addValue(Object ojb) {
            FormulaElement element = new ValueFormulaElement(FormulaOperator.ADD, ojb);
            this.elements.add(element);
            return this;
        }

        /**
         * 加公式
         */
        public FormulaEleBuilder<ParentBuilder> addFormula(Formula formula) {
            FormulaElement element = new FormulaFormulaElement(FormulaOperator.ADD, formula);
            this.elements.add(element);
            return this;
        }

        /**
         * 加关键字
         */
        public FormulaEleBuilder<ParentBuilder> addKeywords(String keywords) {
            FormulaElement element = new KeywordsFormulaElement(FormulaOperator.ADD, keywords);
            this.elements.add(element);
            return this;
        }

        /**
         * 加()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> addGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.ADD, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, this.table, elements);
        }

        /**
         * 减列
         */
        public FormulaEleBuilder<ParentBuilder> subtractColumn(Table table, String column) {
            FormulaElement element = new ColumnFormulaElement(FormulaOperator.SUBTRACT, table, column);
            this.elements.add(element);
            return this;
        }

        /**
         * 减列
         */
        public FormulaEleBuilder<ParentBuilder> subtractColumn(String column) {
            return this.subtractColumn(this.table, column);
        }

        /**
         * 减实体属性
         */
        public FormulaEleBuilder<ParentBuilder> subtractField(EntityTable table, String field) {
            FormulaElement element = new FieldFormulaElement(FormulaOperator.SUBTRACT, table, field);
            this.elements.add(element);
            return this;
        }

        /**
         * 减实体属性
         */
        public FormulaEleBuilder<ParentBuilder> subtractField(String field) {
            this.checkEntityTable();
            return this.subtractField((EntityTable) this.table, field);
        }

        /**
         * 减函数
         */
        public FormulaEleBuilder<ParentBuilder> subtractFun(Function fun) {
            FormulaElement element = new FunFormulaElement(FormulaOperator.SUBTRACT, fun);
            this.elements.add(element);
            return this;
        }

        /**
         * 减自定义值
         */
        public FormulaEleBuilder<ParentBuilder> subtractValue(Object ojb) {
            FormulaElement element = new ValueFormulaElement(FormulaOperator.SUBTRACT, ojb);
            this.elements.add(element);
            return this;
        }

        /**
         * 减公式
         */
        public FormulaEleBuilder<ParentBuilder> subtractFormula(Formula formula) {
            FormulaElement element = new FormulaFormulaElement(FormulaOperator.SUBTRACT, formula);
            this.elements.add(element);
            return this;
        }

        /**
         * 减关键字
         */
        public FormulaEleBuilder<ParentBuilder> subtractKeywords(String keywords) {
            FormulaElement element = new KeywordsFormulaElement(FormulaOperator.SUBTRACT, keywords);
            this.elements.add(element);
            return this;
        }

        /**
         * 减()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> subtractGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.SUBTRACT, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, this.table, elements);
        }

        /**
         * 乘列
         */
        public FormulaEleBuilder<ParentBuilder> multiplyColumn(Table table, String column) {
            FormulaElement element = new ColumnFormulaElement(FormulaOperator.MULTIPLY, table, column);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘列
         */
        public FormulaEleBuilder<ParentBuilder> multiplyColumn(String column) {
            return this.multiplyColumn(this.table, column);
        }

        /**
         * 乘实体属性
         */
        public FormulaEleBuilder<ParentBuilder> multiplyField(EntityTable table, String field) {
            FormulaElement element = new FieldFormulaElement(FormulaOperator.MULTIPLY, table, field);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘实体属性
         */
        public FormulaEleBuilder<ParentBuilder> multiplyField(String field) {
            this.checkEntityTable();
            return this.multiplyField((EntityTable) this.table, field);
        }

        /**
         * 乘函数
         */
        public FormulaEleBuilder<ParentBuilder> multiplyFun(Function fun) {
            FormulaElement element = new FunFormulaElement(FormulaOperator.MULTIPLY, fun);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘自定义值
         */
        public FormulaEleBuilder<ParentBuilder> multiplyValue(Object ojb) {
            FormulaElement element = new ValueFormulaElement(FormulaOperator.MULTIPLY, ojb);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘公式
         */
        public FormulaEleBuilder<ParentBuilder> multiplyFormula(Formula formula) {
            FormulaElement element = new FormulaFormulaElement(FormulaOperator.MULTIPLY, formula);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘关键字
         */
        public FormulaEleBuilder<ParentBuilder> multiplyKeywords(String keywords) {
            FormulaElement element = new KeywordsFormulaElement(FormulaOperator.MULTIPLY, keywords);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> multiplyGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.MULTIPLY, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, this.table, elements);
        }

        /**
         * 除以列
         */
        public FormulaEleBuilder<ParentBuilder> divideColumn(Table table, String column) {
            FormulaElement element = new ColumnFormulaElement(FormulaOperator.DIVIDE, table, column);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以列
         */
        public FormulaEleBuilder<ParentBuilder> divideColumn(String column) {
            return this.divideColumn(this.table, column);
        }

        /**
         * 除以实体属性
         */
        public FormulaEleBuilder<ParentBuilder> divideField(EntityTable table, String field) {
            FormulaElement element = new FieldFormulaElement(FormulaOperator.DIVIDE, table, field);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以实体属性
         */
        public FormulaEleBuilder<ParentBuilder> divideField(String field) {
            this.checkEntityTable();
            return this.divideField((EntityTable) this.table, field);
        }

        /**
         * 除以函数
         */
        public FormulaEleBuilder<ParentBuilder> divideFun(Function fun) {
            FormulaElement element = new FunFormulaElement(FormulaOperator.DIVIDE, fun);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以自定义值
         */
        public FormulaEleBuilder<ParentBuilder> divideValue(Object ojb) {
            FormulaElement element = new ValueFormulaElement(FormulaOperator.DIVIDE, ojb);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以公式
         */
        public FormulaEleBuilder<ParentBuilder> divideFormula(Formula formula) {
            FormulaElement element = new FormulaFormulaElement(FormulaOperator.DIVIDE, formula);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以键字
         */
        public FormulaEleBuilder<ParentBuilder> divideKeywords(String keywords) {
            FormulaElement element = new KeywordsFormulaElement(FormulaOperator.DIVIDE, keywords);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> divideGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.DIVIDE, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, this.table, elements);
        }
    }
}
