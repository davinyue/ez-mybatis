package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class ResultMapInterceptor implements Interceptor {
    private static final Field resultMapsField = ReflectionUtils.getField(MappedStatement.class,
            "resultMaps");

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof Executor)) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        ResultMap resultMap = ms.getResultMaps().get(0);
        if (resultMap.getResultMappings() != null && !resultMap.getResultMappings().isEmpty()) {
            return invocation.proceed();
        }
        if (resultMap.getId().startsWith(EzMapper.class.getName())) {
            Map<String, Object> param = (Map<String, Object>) invocation.getArgs()[1];
            EzParam ezParam = (EzParam) param.get(EzMybatisConstant.MAPPER_PARAM_QUERY);
            ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(),
                    ezParam.getTable().getEtType(), resultMap.getResultMappings()).build();
            ReflectionUtils.setFieldValue(ms, resultMapsField, Collections.singletonList(newRm), false);
        }
        return invocation.proceed();
    }
}
