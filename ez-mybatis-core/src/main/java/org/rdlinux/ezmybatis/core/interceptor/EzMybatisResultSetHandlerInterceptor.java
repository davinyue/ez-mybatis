package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.rdlinux.ezmybatis.core.interceptor.resultsethandler.ResultSetLogic;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * 结果集处理拦截器。
 *
 * <p>该拦截器拦截 MyBatis 的 {@code handleResultSets} 调用，并将满足条件的 Mapper
 * 结果处理过程切换到 Ez-MyBatis 自定义的结果集处理逻辑。</p>
 */
@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class EzMybatisResultSetHandlerInterceptor extends AbstractInterceptor {
    /**
     * 当前拦截器持有的处理逻辑链。
     */
    protected List<InterceptorLogic> logics = new LinkedList<>();

    /**
     * 创建结果集处理拦截器，并注册默认结果集替换逻辑。
     */
    public EzMybatisResultSetHandlerInterceptor() {
        this.logics.add(new ResultSetLogic());
    }

    /**
     * 获取当前拦截器的逻辑链。
     *
     * @return 逻辑链列表
     */
    @Override
    public List<InterceptorLogic> getLogics() {
        return this.logics;
    }
}
