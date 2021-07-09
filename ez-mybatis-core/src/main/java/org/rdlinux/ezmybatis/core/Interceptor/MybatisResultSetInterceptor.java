package org.rdlinux.ezmybatis.core.Interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.rdlinux.ezmybatis.core.EzResultSetHandler;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.sql.Statement;

@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class MybatisResultSetInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        if (invocation.getTarget() instanceof DefaultResultSetHandler) {
            Executor executor = ReflectionUtils.getFieldValue(resultSetHandler, "executor");
            MappedStatement mappedStatement = ReflectionUtils.getFieldValue(resultSetHandler,
                    "mappedStatement");
            ParameterHandler parameterHandler = ReflectionUtils.getFieldValue(resultSetHandler,
                    "parameterHandler");
            ResultHandler<?> resultHandler = ReflectionUtils.getFieldValue(resultSetHandler, "resultHandler");
            BoundSql boundSql = ReflectionUtils.getFieldValue(resultSetHandler, "boundSql");
            RowBounds rowBounds = ReflectionUtils.getFieldValue(resultSetHandler, "rowBounds");
            EzResultSetHandler ezResultSetHandler = new EzResultSetHandler(executor, mappedStatement, parameterHandler,
                    resultHandler, boundSql, rowBounds);
            return ezResultSetHandler.handleResultSets(statement);
        }
        return invocation.proceed();
    }
}
