package org.rdlinux.ezmybatis.core.interceptor.executor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogic;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogicResult;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeHandlerInitLogic implements InterceptorLogic {
    private static final ThreadLocal<Map<String, TypeHandler<?>>> PARAM_MAP_TYPE_HANDLER = ThreadLocal.withInitial(HashMap::new);

    public static void registerTypeHandler(String paramName, TypeHandler<?> typeHandler) {
        PARAM_MAP_TYPE_HANDLER.get().put(paramName, typeHandler);
    }

    @Override
    public InterceptorLogicResult invokeBefore(Invocation invocation) {
        try {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = handler.getBoundSql();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (ParameterMapping parameterMapping : parameterMappings) {
                TypeHandler<?> typeHandler = PARAM_MAP_TYPE_HANDLER.get().get(parameterMapping.getProperty());
                if (typeHandler != null) {
                    ReflectionUtils.setFieldValue(parameterMapping, "typeHandler", typeHandler);
                }
            }
            return new InterceptorLogicResult(true, null);
        } finally {
            PARAM_MAP_TYPE_HANDLER.remove();
        }
    }
}