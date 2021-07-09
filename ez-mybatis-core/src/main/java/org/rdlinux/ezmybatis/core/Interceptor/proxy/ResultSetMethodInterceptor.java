package org.rdlinux.ezmybatis.core.Interceptor.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public class ResultSetMethodInterceptor {
    public static DefaultResultSetHandler test(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler,
                                               ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultResultSetHandler.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println(method.getName());
            Object result = proxy.invokeSuper(obj, args);
            System.out.println("after method run...");
            return result;
        });
        DefaultResultSetHandler sample = (DefaultResultSetHandler) enhancer.create(
                new Class[]{Executor.class, MappedStatement.class, ParameterHandler.class, ResultHandler.class,
                        BoundSql.class, RowBounds.class},
                new Object[]{executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds});
        return sample;
    }

}
