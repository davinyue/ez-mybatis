package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetNeedAlias;
import org.rdlinux.ezmybatis.enumeration.FormulaOperator;

import java.util.LinkedList;
import java.util.List;

/**
 * 计算公式
 */
@Getter
public class Formula implements QueryRetNeedAlias {
    /**
     * 公式要素
     */
    private List<FormulaElement> elements;

    private Formula() {
    }

    public static FormulaEleBuilder<FormulaBuilder> builder() {
        List<FormulaElement> elements = new LinkedList<>();
        FormulaBuilder formulaBuilder = new FormulaBuilder(elements);
        return new FormulaEleBuilder<>(formulaBuilder, elements);
    }

    public static FormulaEleBuilder<FormulaBuilder> builder(Operand value) {
        return builder().with(value);
    }

    public static FormulaEleBuilder<FormulaBuilder> builder(Object value) {
        return builder().with(value);
    }

    public static class FormulaBuilder {
        private final Formula formula;

        public FormulaBuilder(List<FormulaElement> elements) {
            this.formula = new Formula();
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

        public FormulaEleBuilder(ParentBuilder parentBuilder, List<FormulaElement> elements) {
            this.parentBuilder = parentBuilder;
            this.elements = elements;
        }

        public ParentBuilder done() {
            return this.parentBuilder;
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
         * 以自定义操作数开始, 构建计算公式
         */
        public FormulaEleBuilder<ParentBuilder> with(Object value) {
            return this.with(Operand.objToOperand(value));
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
            return new FormulaEleBuilder<>(this, elements);
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
         * 加自定义操作数
         */
        public FormulaEleBuilder<ParentBuilder> add(Object value) {
            return this.add(Operand.objToOperand(value));
        }

        /**
         * 加()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> addGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.ADD, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, elements);
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
         * 减自定义操作数
         */
        public FormulaEleBuilder<ParentBuilder> subtract(Object value) {
            return this.subtract(Operand.objToOperand(value));
        }

        /**
         * 减()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> subtractGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.SUBTRACT, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, elements);
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
         * 乘自定义操作数
         */
        public FormulaEleBuilder<ParentBuilder> multiply(Object value) {
            return this.multiply(Operand.objToOperand(value));
        }

        /**
         * 乘()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> multiplyGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.MULTIPLY, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, elements);
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
         * 除自定义操作数
         */
        public FormulaEleBuilder<ParentBuilder> divide(Object value) {
            return this.divide(Operand.objToOperand(value));
        }

        /**
         * 除以()
         */
        public FormulaEleBuilder<FormulaEleBuilder<ParentBuilder>> divideGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.DIVIDE, elements);
            this.elements.add(element);
            return new FormulaEleBuilder<>(this, elements);
        }
    }
}
