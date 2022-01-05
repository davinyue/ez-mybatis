package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.utils.Assert;

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

    public String getParamName(Object paramValue) {
        Assert.notNull(paramValue, "paramValue can not be null");
        String escape = getEscapeChar(paramValue);
        String paramName = EzMybatisConstant.MAPPER_PARAM_EZPARAM + "_" + this.pNo.getAndIncrement();
        this.mybatisParam.put(paramName, paramValue);
        return escape + "{" + paramName + "}";
    }
}
