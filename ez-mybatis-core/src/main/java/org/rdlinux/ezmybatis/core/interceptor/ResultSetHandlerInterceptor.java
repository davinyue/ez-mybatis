package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.rdlinux.ezmybatis.core.interceptor.resultsethandler.ResultSetLogic;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * 结果处理拦截器
 */
@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class ResultSetHandlerInterceptor extends AbstractInterceptor {
    private static List<InterceptorLogic> logics;

    static {
        logics = new LinkedList<>();
        logics.add(new ResultSetLogic());
    }

    @Override
    public List<InterceptorLogic> getLogics() {
        return logics;
    }
}
