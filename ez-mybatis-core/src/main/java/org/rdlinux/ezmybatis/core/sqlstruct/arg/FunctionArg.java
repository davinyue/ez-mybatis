package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 函数参数
 */
public class FunctionArg implements Arg {
    private Function function;

    private FunctionArg(Function function) {
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }

    public static FunctionArg of(Function function) {
        return new FunctionArg(function);
    }

    public Function getFunction() {
        return this.function;
    }
}
