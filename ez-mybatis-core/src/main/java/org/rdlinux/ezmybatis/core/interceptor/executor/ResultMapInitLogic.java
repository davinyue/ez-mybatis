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
    private static final Field hasNestedResultMapsFiled = ReflectionUtils.getField(ResultMap.class,
            "hasNestedResultMaps");

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
            //泛型接口, 需要动态的设置返回结果类型，这两个接口的返回类型由query参数传入
            if (resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.QUERY_METHOD + "-") ||
                    resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.QUERY_ONE_METHOD + "-")) {
                Map<String, Object> param = (Map<String, Object>) invocation.getArgs()[1];
                EzParam<?> ezParam = (EzParam<?>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
                ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(),
                        ezParam.getRetType(), resultMap.getResultMappings()).build();
                ReflectionUtils.setFieldValue(ms, resultMapsField, Collections.singletonList(newRm), false);
            }
            //泛型接口, 需要动态的设置返回结果类型，这两个接口的返回类型是由参数参入的
            else if (resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.SELECT_BY_ID_METHOD
                    + "-") ||
                    resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.SELECT_BY_IDS_METHOD
                            + "-")) {
                Map<String, Object> param = (Map<String, Object>) invocation.getArgs()[1];
                Class<?> entityClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
                ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(),
                        entityClass, resultMap.getResultMappings()).build();
                ReflectionUtils.setFieldValue(ms, resultMapsField, Collections.singletonList(newRm), false);
            }
            //查询count, 需要把hasNestedResultMaps设置为false, 才能解析结果
            else if (resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.QUERY_COUNT_METHOD
                    + "-")) {
                ReflectionUtils.setFieldValue(resultMap, hasNestedResultMapsFiled, false);
            }
        }
        return new InterceptorLogicResult(true, null);
    }
}
