package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.utils.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MybatisParamHolder {
    private Map<String, Object> mybatisParam;
    private AtomicInteger pNo = new AtomicInteger(0);

    public MybatisParamHolder(Map<String, Object> mybatisParam) {
        this.mybatisParam = mybatisParam;
    }

    private static String getEscapeChar(Object param) {
        if (param instanceof CharSequence || param instanceof Date || param instanceof LocalTime
                || param instanceof LocalDate || param instanceof LocalDateTime || param instanceof Enum) {
            return "#";
        } else {
            return "$";
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
