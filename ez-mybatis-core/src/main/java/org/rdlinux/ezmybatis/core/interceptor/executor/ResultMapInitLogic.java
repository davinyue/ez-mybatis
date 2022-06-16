package org.rdlinux.ezmybatis.core.interceptor.executor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Invocation;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogic;
import org.rdlinux.ezmybatis.core.interceptor.InterceptorLogicResult;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ResultMapInitLogic implements InterceptorLogic {
    /**
     * 返回结果
     */
    public static final ThreadLocal<Class<?>> RET_TYPE_TL = new ThreadLocal<>();
    private static final Field hasNestedResultMapsFiled = ReflectionUtils.getField(ResultMap.class,
            "hasNestedResultMaps");

    /**
     * 获取结果类型
     */
    public static Class<?> getRetType(ResultMap resultMap) {
        Class<?> retType = RET_TYPE_TL.get();
        if (retType == null) {
            retType = resultMap.getType();
        }
        return retType;
    }

    /**
     * 清空结果类型
     */
    public static void cleanRetType() {
        RET_TYPE_TL.remove();
    }

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public InterceptorLogicResult invokeBefore(Invocation invocation) {
        cleanRetType();
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
                RET_TYPE_TL.set(ezParam.getRetType());
                ReflectionUtils.setFieldValue(resultMap, hasNestedResultMapsFiled, false);
            }
            //泛型接口, 需要动态的设置返回结果类型，这两个接口的返回类型是由参数参入的
            else if (resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.SELECT_BY_ID_METHOD
                    + "-") ||
                    resultMap.getId().startsWith(EzMapper.class.getName() + "." + EzMapper.SELECT_BY_IDS_METHOD
                            + "-")) {
                Map<String, Object> param = (Map<String, Object>) invocation.getArgs()[1];
                Class<?> entityClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
                RET_TYPE_TL.set(entityClass);
                ReflectionUtils.setFieldValue(resultMap, hasNestedResultMapsFiled, false);
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
