package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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
     * 通过闭包 Lambda 直接构建出一个函数
     *
     * @param funName  函数名称，例如 "MAX", "COUNT", "CONCAT"
     * @param consumer 函数参数配置闭包
     * @return 构建完成的 Function 对象
     */
    public static Function build(String funName, Consumer<FunctionBuilder> consumer) {
        FunctionBuilder builder = new FunctionBuilder(funName);
        consumer.accept(builder);
        return builder.build();
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
         * 根据条件延迟构造并添加函数参数。
         *
         * <p>适用于参数构造本身依赖于 {@code sure} 判定参数的场景。只有在 {@code sure=true}
         * 时才会执行回调，避免在 {@code sure=false} 时提前触发诸如 {@code table.field(a)}
         * 之类的参数校验或异常。</p>
         *
         * @param sure 是否满足条件，为 true 时才会执行回调
         * @param cb   参数构造回调
         * @return 当前构造器
         */
        public FunctionBuilder addArg(boolean sure, Consumer<FunctionBuilder> cb) {
            if (sure) {
                cb.accept(this);
            }
            return this;
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
         * 根据条件延迟构造并添加带 DISTINCT 关键字的函数参数。
         *
         * <p>适用于参数构造本身依赖于 {@code sure} 判定参数的场景。只有在 {@code sure=true}
         * 时才会执行回调，避免在 {@code sure=false} 时提前触发诸如 {@code table.field(a)}
         * 之类的参数校验或异常。</p>
         *
         * @param sure 是否满足条件，为 true 时才会执行回调
         * @param cb   参数构造回调
         * @return 当前构造器
         */
        public FunctionBuilder addDistinctArg(boolean sure, Consumer<FunctionBuilder> cb) {
            if (sure) {
                cb.accept(this);
            }
            return this;
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
        private Function build() {
            return this.function;
        }
    }
}
