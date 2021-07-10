package org.rdlinux.ezmybatis.core.interceptor.resultsethandler;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.rdlinux.ezmybatis.core.EzResultSetHandler;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogic;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogicResult;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.sql.Statement;

public class ResultSetLogic implements InterceptorLogic {
    @Override
    public InterceptorLogicResult invokeBefore(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        if (invocation.getTarget() instanceof DefaultResultSetHandler) {
            Executor executor = ReflectionUtils.getFieldValue(resultSetHandler, "executor");
            MappedStatement mappedStatement = ReflectionUtils.getFieldValue(resultSetHandler,
                    "mappedStatement");
            ParameterHandler parameterHandler = ReflectionUtils.getFieldValue(resultSetHandler,
                    "parameterHandler");
            ResultHandler<?> resultHandler = ReflectionUtils.getFieldValue(resultSetHandler,
                    "resultHandler");
            BoundSql boundSql = ReflectionUtils.getFieldValue(resultSetHandler, "boundSql");
            RowBounds rowBounds = ReflectionUtils.getFieldValue(resultSetHandler, "rowBounds");
            EzResultSetHandler ezResultSetHandler = new EzResultSetHandler(executor, mappedStatement, parameterHandler,
                    resultHandler, boundSql, rowBounds);
            return new InterceptorLogicResult(false, ezResultSetHandler.handleResultSets(statement));
        }
        return new InterceptorLogicResult(true, null);
    }
}
