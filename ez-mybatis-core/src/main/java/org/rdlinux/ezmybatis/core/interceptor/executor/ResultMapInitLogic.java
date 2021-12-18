package org.rdlinux.ezmybatis.core.interceptor.executor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Invocation;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogic;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogicResult;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class ResultMapInitLogic implements InterceptorLogic {
    private static final Field resultMapsField = ReflectionUtils.getField(MappedStatement.class,
            "resultMaps");

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public InterceptorLogicResult invokeBefore(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof Executor)) {
            return new InterceptorLogicResult(true, null);
        }
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        if (ms.getResultMaps() == null) {
            return new InterceptorLogicResult(true, null);
        }
        for (ResultMap resultMap : ms.getResultMaps()) {
            if (resultMap.getResultMappings() != null && !resultMap.getResultMappings().isEmpty()) {
                continue;
            }
            if (!resultMap.getType().getName().equals(Object.class.getName())) {
                continue;
            }
            if (resultMap.getId().startsWith(EzMapper.class.getName())) {
                Map<String, Object> param = (Map<String, Object>) invocation.getArgs()[1];
                EzParam<?> ezParam = (EzParam<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
                ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(),
                        ezParam.getRetType(), resultMap.getResultMappings()).build();
                ReflectionUtils.setFieldValue(ms, resultMapsField, Collections.singletonList(newRm), false);
            }
        }
        return new InterceptorLogicResult(true, null);
    }
}
