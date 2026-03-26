package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * 函数
 */
@Getter
public class Function implements QueryRetNeedAlias {

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

    /**
     * 获取函数构造器
     *
     * @param funName 函数名称，例如 "MAX", "COUNT", "CONCAT"
     * @return 函数构造器
     */
    public static FunctionBuilder builder(String funName) {
        return new FunctionBuilder(funName);
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
        private final Function function;

        private FunctionBuilder(String funName) {
            Assert.notEmpty(funName, "function name can not be null or empty");
            this.function = new Function();
            this.function.funName = funName;
            this.function.funArgs = new LinkedList<>();
        }

        /**
         * 根据条件添加函数参数
         *
         * @param sure 是否满足条件，为 true 时才会实际添加该参数
         * @param arg  标准的结构化参数 (Operand)
         */
        public FunctionBuilder addArg(boolean sure, Operand arg) {
            if (!sure) {
                return this;
            }
            Assert.notNull(arg, "arg can not be null");
            FunArg funArg = new FunArg().setArgValue(arg).setDistinct(false);
            this.function.funArgs.add(funArg);
            return this;
        }

        /**
         * 添加函数参数
         *
         * @param arg 标准的结构化参数 (Operand)
         */
        public FunctionBuilder addArg(Operand arg) {
            return this.addArg(true, arg);
        }

        /**
         * 根据条件添加通用的函数参数（如对象、常量等，支持自动包装为 Operand）
         *
         * @param sure 是否满足条件，为 true 时才会实际添加该参数
         * @param arg  通用对象参数，会被自动包装为对应的 Operand 对象
         */
        public FunctionBuilder addArg(boolean sure, Object arg) {
            return this.addArg(sure, Operand.objToOperand(arg));
        }

        /**
         * 添加通用的函数参数（如对象、常量等，支持自动包装为 Operand）
         *
         * @param arg 通用对象参数
         */
        public FunctionBuilder addArg(Object arg) {
            return this.addArg(true, arg);
        }

        /**
         * 根据条件添加带 DISTINCT 关键字的函数去重参数
         *
         * @param sure 是否满足条件，为 true 时才会实际添加该参数
         * @param arg  标准的结构化参数 (Operand)
         */
        public FunctionBuilder addDistinctArg(boolean sure, Operand arg) {
            if (!sure) {
                return this;
            }
            Assert.notNull(arg, "arg can not be null");
            FunArg funArg = new FunArg().setArgValue(arg).setDistinct(true);
            this.function.funArgs.add(funArg);
            return this;
        }

        /**
         * 添加带 DISTINCT 关键字的函数去重参数
         *
         * @param arg 标准的结构化参数 (Operand)
         */
        public FunctionBuilder addDistinctArg(Operand arg) {
            return this.addDistinctArg(true, arg);
        }

        /**
         * 根据条件添加带 DISTINCT 关键字的通用函数去重参数
         *
         * @param sure 是否满足条件，为 true 时才会实际添加该参数
         * @param arg  通用对象参数，会被自动包装为对应的 Operand 对象
         */
        public FunctionBuilder addDistinctArg(boolean sure, Object arg) {
            return this.addDistinctArg(sure, Operand.objToOperand(arg));
        }

        /**
         * 添加带 DISTINCT 关键字的通用函数去重参数
         *
         * @param arg 通用对象参数
         */
        public FunctionBuilder addDistinctArg(Object arg) {
            return this.addDistinctArg(true, arg);
        }

        /**
         * 构建并返回最终的 Function 实例
         *
         * @return Function 实例
         */
        public Function build() {
            return this.function;
        }
    }
}
