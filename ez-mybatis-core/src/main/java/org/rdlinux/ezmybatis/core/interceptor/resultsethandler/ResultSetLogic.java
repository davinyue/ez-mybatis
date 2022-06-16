package org.rdlinux.ezmybatis.core.interceptor.resultsethandler;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogic;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogicResult;
import org.rdlinux.ezmybatis.core.mapper.EzBaseMapper;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Statement;

public class ResultSetLogic implements InterceptorLogic {
    private static final Field executorField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "executor");
    private static final Field mappedStatementField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "mappedStatement");
    private static final Field parameterHandlerField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "parameterHandler");
    private static final Field resultHandlerField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "resultHandler");
    private static final Field boundSqlField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "boundSql");
    private static final Field rowBoundsField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "rowBounds");

    @Override
    public InterceptorLogicResult invokeBefore(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
            MappedStatement mappedStatement = ReflectionUtils.getFieldValue(resultSetHandler,
                    "mappedStatement");
            String methodId = mappedStatement.getId();
            int index = methodId.lastIndexOf(".");
            String className = methodId.substring(0, index);
            Class<?> mapperClass = Class.forName(className);
            if (!EzBaseMapper.class.isAssignableFrom(mapperClass) && !EzMapper.class.isAssignableFrom(mapperClass)) {
                return new InterceptorLogicResult(true, null);
            }
            Executor executor = ReflectionUtils.getFieldValue(resultSetHandler, executorField);
            MappedStatement ms = ReflectionUtils.getFieldValue(resultSetHandler, mappedStatementField);
            ParameterHandler parameterHandler = ReflectionUtils.getFieldValue(resultSetHandler, parameterHandlerField);
            ResultHandler<?> resultHandler = ReflectionUtils.getFieldValue(resultSetHandler, resultHandlerField);
            BoundSql boundSql = ReflectionUtils.getFieldValue(resultSetHandler, boundSqlField);
            RowBounds rowBounds = ReflectionUtils.getFieldValue(resultSetHandler, rowBoundsField);
            EzResultSetHandler ezResultSetHandler = new EzResultSetHandler(executor, ms, parameterHandler,
                    resultHandler, boundSql, rowBounds);
            Statement statement = (Statement) invocation.getArgs()[0];
            return new InterceptorLogicResult(false, ezResultSetHandler.handleResultSets(statement));
        }
        return new InterceptorLogicResult(true, null);
    }
}
