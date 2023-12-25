package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

/**
 * 计算公式
 */
@Getter
public class Formula implements QueryRetNeedAlias {
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
         * 以列开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> with(Operand value) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.EMPTY, value);
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.EMPTY, TableColumn.of(table, column));
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
        public FormulaEleBuilder<ParentBuilder> withColumn(String column) {
            return this.withColumn(this.table, column);
        }

        /**
         * 以实体属性开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withField(EntityTable table, String field) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.EMPTY, EntityField.of(table, field));
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
        public FormulaEleBuilder<ParentBuilder> withField(String field) {
            this.checkEntityTable();
            return this.withField((EntityTable) this.table, field);
        }

        /**
         * 以自定义值开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> withValue(Object ojb) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.EMPTY, ObjArg.of(ojb));
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.EMPTY, Keywords.of(keywords));
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return this;
        }

        /**
         * 以()开始, 构建计算公式
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> withGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.EMPTY, elements);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return new FormulaEleBuilder<>(this, this.table, elements);
        }

        /**
         * 加
         */
        public FormulaEleBuilder<ParentBuilder> add(Operand value) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.ADD, value);
            this.elements.add(element);
            return this;
        }

        /**
         * 加列
         */
        public FormulaEleBuilder<ParentBuilder> addColumn(Table table, String column) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.ADD, TableColumn.of(table, column));
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.ADD, EntityField.of(table, field));
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
         * 加自定义值
         */
        public FormulaEleBuilder<ParentBuilder> addValue(Object ojb) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.ADD, ObjArg.of(ojb));
            this.elements.add(element);
            return this;
        }


        /**
         * 加关键字
         */
        public FormulaEleBuilder<ParentBuilder> addKeywords(String keywords) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.ADD, Keywords.of(keywords));
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
         * 减
         */
        public FormulaEleBuilder<ParentBuilder> subtract(Operand value) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.SUBTRACT, value);
            this.elements.add(element);
            return this;
        }

        /**
         * 减列
         */
        public FormulaEleBuilder<ParentBuilder> subtractColumn(Table table, String column) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.SUBTRACT, TableColumn.of(table, column));
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.SUBTRACT, EntityField.of(table, field));
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
         * 减自定义值
         */
        public FormulaEleBuilder<ParentBuilder> subtractValue(Object ojb) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.SUBTRACT, ObjArg.of(ojb));
            this.elements.add(element);
            return this;
        }

        /**
         * 减关键字
         */
        public FormulaEleBuilder<ParentBuilder> subtractKeywords(String keywords) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.SUBTRACT, Keywords.of(keywords));
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
         * 乘
         */
        public FormulaEleBuilder<ParentBuilder> multiply(Operand value) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.MULTIPLY, value);
            this.elements.add(element);
            return this;
        }

        /**
         * 乘列
         */
        public FormulaEleBuilder<ParentBuilder> multiplyColumn(Table table, String column) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.MULTIPLY, TableColumn.of(table, column));
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.MULTIPLY, EntityField.of(table, field));
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
         * 乘自定义值
         */
        public FormulaEleBuilder<ParentBuilder> multiplyValue(Object ojb) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.MULTIPLY, ObjArg.of(ojb));
            this.elements.add(element);
            return this;
        }

        /**
         * 乘关键字
         */
        public FormulaEleBuilder<ParentBuilder> multiplyKeywords(String keywords) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.MULTIPLY, Keywords.of(keywords));
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
         * 除
         */
        public FormulaEleBuilder<ParentBuilder> divide(Operand value) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.DIVIDE, value);
            this.elements.add(element);
            return this;
        }

        /**
         * 除以列
         */
        public FormulaEleBuilder<ParentBuilder> divideColumn(Table table, String column) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.DIVIDE, TableColumn.of(table, column));
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
            FormulaElement element = new FormulaOperandElement(FormulaOperator.DIVIDE, EntityField.of(table, field));
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
         * 除以自定义值
         */
        public FormulaEleBuilder<ParentBuilder> divideValue(Object ojb) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.DIVIDE, ObjArg.of(ojb));
            this.elements.add(element);
            return this;
        }

        /**
         * 除以键字
         */
        public FormulaEleBuilder<ParentBuilder> divideKeywords(String keywords) {
            FormulaElement element = new FormulaOperandElement(FormulaOperator.DIVIDE, Keywords.of(keywords));
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
