package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.EzMybatisConstant;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MybatisParamHolder {
    private Map<String, Object> mybatisParam;
    private AtomicInteger pNo = new AtomicInteger(0);

    public MybatisParamHolder(Map<String, Object> mybatisParam) {
        this.mybatisParam = mybatisParam;
    }

    private static String getEscapeChar(Object param) {
        if (param instanceof Number) {
            return "$";
        } else {
            return "#";
        }
    }

    @SuppressWarnings(value = {"unchecked"})
    public <T> T get(String param) {
        return (T) this.mybatisParam.get(param);
    }

    public String getParamName(Object paramValue, boolean canBeNull) {
        if (canBeNull && paramValue == null) {
            return "NULL";
        } else if (!canBeNull && paramValue == null) {
            throw new IllegalArgumentException("paramValue can not be null");
        }
        String escape = getEscapeChar(paramValue);
        String paramName = EzMybatisConstant.MAPPER_PARAM_EZPARAM + "_" + this.pNo.getAndIncrement();
        this.mybatisParam.put(paramName, paramValue);
        return escape + "{" + paramName + "}";
    }
}
