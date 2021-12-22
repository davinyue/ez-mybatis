package ink.dvc.ezmybatis.core.interceptor;

import ink.dvc.ezmybatis.core.interceptor.resultsethandler.ResultSetLogic;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

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
    protected List<InterceptorLogic> logics = new LinkedList<>();

    public ResultSetHandlerInterceptor() {
        this.logics.add(new ResultSetLogic());
    }

    @Override
    public List<InterceptorLogic> getLogics() {
        return this.logics;
    }
}
