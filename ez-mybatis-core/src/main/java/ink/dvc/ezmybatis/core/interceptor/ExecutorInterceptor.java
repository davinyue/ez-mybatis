package ink.dvc.ezmybatis.core.interceptor;

import ink.dvc.ezmybatis.core.interceptor.executor.MapperParamInitLogic;
import ink.dvc.ezmybatis.core.interceptor.executor.ResultMapInitLogic;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.LinkedList;
import java.util.List;

/**
 * mapper接口的参数初始化拦截器
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
                        BoundSql.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "queryCursor",
                args = {MappedStatement.class, Object.class, RowBounds.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class ExecutorInterceptor extends AbstractInterceptor {
    private static List<InterceptorLogic> logics = new LinkedList<>();

    static {
        //添加参数处理, 然后添加结果类型处理, 二者顺序不能颠倒
        logics.add(new MapperParamInitLogic());
        logics.add(new ResultMapInitLogic());
    }

    @Override
    public List<InterceptorLogic> getLogics() {
        return logics;
    }
}
