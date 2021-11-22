package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.utils.Assert;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MybatisParamHolder {
    private Map<String, Object> mybatisParam;
    private AtomicInteger pNo = new AtomicInteger(0);

    public MybatisParamHolder(Map<String, Object> mybatisParam) {
        this.mybatisParam = mybatisParam;
    }

    public String getParamName(Object paramValue) {
        Assert.notNull(paramValue, "paramValue can not be null");
        String paramName = EzMybatisConstant.MAPPER_PARAM_QUERY + "_" + this.pNo.getAndIncrement();
        this.mybatisParam.put(paramName, paramValue);
        return paramName;
    }
}
