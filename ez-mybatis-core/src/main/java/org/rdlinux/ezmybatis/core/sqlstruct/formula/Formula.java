package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetNeedAlias;
import org.rdlinux.ezmybatis.enumeration.FormulaOperator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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

    /**
     * 通过闭包 Lambda 直接构建出一个计算公式，避免后缀链式的 .done().build()
     */
    public static Formula build(Consumer<FormulaStartBuilder<FormulaBuilder>> consumer) {
        List<FormulaElement> elements = new LinkedList<>();
        FormulaBuilder formulaBuilder = new FormulaBuilder(elements);
        consumer.accept(new FormulaStartBuilder<>(formulaBuilder, elements));
        return formulaBuilder.build();
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
     * 起始构造器，限定起始操作必须是 with 或 withGroup
     *
     * @param <ParentBuilder> 上级构造器
     */
    public static class FormulaStartBuilder<ParentBuilder> {
        protected ParentBuilder parentBuilder;
        protected List<FormulaElement> elements;

        public FormulaStartBuilder(ParentBuilder parentBuilder, List<FormulaElement> elements) {
            this.parentBuilder = parentBuilder;
            this.elements = elements;
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
            return new FormulaEleBuilder<>(this.parentBuilder, this.elements);
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
        public FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> withGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.EMPTY, elements);
            if (this.elements.isEmpty()) {
                this.elements.add(element);
            } else {
                this.elements.set(0, element);
            }
            return new FormulaStartBuilder<>(new FormulaEleBuilder<>(this.parentBuilder, this.elements), elements);
        }

        /**
         * 以()开始, 构建计算公式 (Lambda 闭包形式)
         */
        public FormulaEleBuilder<ParentBuilder> withGroup(Consumer<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>> consumer) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.withGroup();
            consumer.accept(childBuilder);
            return new FormulaEleBuilder<>(this.parentBuilder, this.elements);
        }

        /**
         * 以()开始, 构建计算公式 (Lambda 闭包形式，支持带返回值的表达式)
         */
        public FormulaEleBuilder<ParentBuilder> withGroup(java.util.function.Function<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>, ?> function) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.withGroup();
            function.apply(childBuilder);
            return new FormulaEleBuilder<>(this.parentBuilder, this.elements);
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
        private FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> addGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.ADD, elements);
            this.elements.add(element);
            return new FormulaStartBuilder<>(this, elements);
        }

        /**
         * 加() (Lambda 闭包形式)
         */
        public FormulaEleBuilder<ParentBuilder> addGroup(Consumer<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>> consumer) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.addGroup();
            consumer.accept(childBuilder);
            return this;
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
        private FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> subtractGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.SUBTRACT, elements);
            this.elements.add(element);
            return new FormulaStartBuilder<>(this, elements);
        }

        /**
         * 减() (Lambda 闭包形式)
         */
        public FormulaEleBuilder<ParentBuilder> subtractGroup(Consumer<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>> consumer) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.subtractGroup();
            consumer.accept(childBuilder);
            return this;
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
        private FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> multiplyGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.MULTIPLY, elements);
            this.elements.add(element);
            return new FormulaStartBuilder<>(this, elements);
        }

        /**
         * 乘() (Lambda 闭包形式)
         */
        public FormulaEleBuilder<ParentBuilder> multiplyGroup(Consumer<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>> consumer) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.multiplyGroup();
            consumer.accept(childBuilder);
            return this;
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
        private FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> divideGroup() {
            List<FormulaElement> elements = new LinkedList<>();
            GroupFormulaElement element = new GroupFormulaElement(FormulaOperator.DIVIDE, elements);
            this.elements.add(element);
            return new FormulaStartBuilder<>(this, elements);
        }

        /**
         * 除以() (Lambda 闭包形式)
         */
        public FormulaEleBuilder<ParentBuilder> divideGroup(Consumer<FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>>> consumer) {
            FormulaStartBuilder<FormulaEleBuilder<ParentBuilder>> childBuilder = this.divideGroup();
            consumer.accept(childBuilder);
            return this;
        }
    }
}
