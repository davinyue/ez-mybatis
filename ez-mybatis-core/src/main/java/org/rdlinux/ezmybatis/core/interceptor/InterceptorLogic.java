package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.plugin.Invocation;

public interface InterceptorLogic {
    InterceptorLogicResult invokeBefore(Invocation invocation) throws Throwable;
}
