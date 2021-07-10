package org.rdlinux.ezmybatis.core.Interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

//StatementHandler/setParameters,ParameterHandler/parameterize
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class MybatisParameterInterceptor implements Interceptor {

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        return invocation.proceed();
    }
}
