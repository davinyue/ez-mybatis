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

/**
 * 结果集处理替换逻辑。
 *
 * <p>当目标对象为 MyBatis 默认的 {@link DefaultResultSetHandler} 且当前 Mapper 属于
 * Ez-MyBatis 的通用 Mapper 体系时，该逻辑会提取原始处理器中的执行上下文，
 * 转交给 {@link EzResultSetHandler} 执行。</p>
 */
public class ResultSetLogic implements InterceptorLogic {
    /**
     * 默认结果集处理器中的执行器字段。
     */
    private static final Field executorField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "executor");
    /**
     * 默认结果集处理器中的映射语句字段。
     */
    private static final Field mappedStatementField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "mappedStatement");
    /**
     * 默认结果集处理器中的参数处理器字段。
     */
    private static final Field parameterHandlerField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "parameterHandler");
    /**
     * 默认结果集处理器中的结果处理器字段。
     */
    private static final Field resultHandlerField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "resultHandler");
    /**
     * 默认结果集处理器中的绑定 SQL 字段。
     */
    private static final Field boundSqlField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "boundSql");
    /**
     * 默认结果集处理器中的分页参数字段。
     */
    private static final Field rowBoundsField = ReflectionUtils.getField(DefaultResultSetHandler.class,
            "rowBounds");

    /**
     * 在调用前判断是否需要切换为 Ez-MyBatis 自定义结果集处理器。
     *
     * @param invocation 当前拦截调用
     * @return 拦截逻辑结果
     * @throws Throwable 反射或结果处理异常
     */
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
