package org.rdlinux;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder.MybatisParamInfo;

import java.util.HashMap;
import java.util.Map;

public class MybatisParamHolderTest {
    @Test
    public void shouldReuseCurrentArrayAndIncrementIndexes() {
        Map<String, Object> paramMap = new HashMap<>();
        MybatisParamHolder paramHolder = new MybatisParamHolder(null, paramMap);

        MybatisParamInfo first = paramHolder.getMybatisParamName("a");
        MybatisParamInfo second = paramHolder.getMybatisParamName("a");
        MybatisParamInfo third = paramHolder.getMybatisParamName(1);

        String paramPrefix = EzMybatisConstant.MAPPER_PARAM_EZPARAM + "_0";
        Assert.assertEquals("#{" + paramPrefix + "[0]}", first.getFormatedName());
        Assert.assertEquals("#{" + paramPrefix + "[1]}", second.getFormatedName());
        Assert.assertEquals("${" + paramPrefix + "[2]}", third.getFormatedName());
        Assert.assertTrue(paramMap.containsKey(paramPrefix));
    }

    @Test
    public void shouldReturnNullPlaceholderForNullValue() {
        MybatisParamHolder paramHolder = new MybatisParamHolder(null, new HashMap<>());

        MybatisParamInfo paramInfo = paramHolder.getMybatisParamName(null);

        Assert.assertEquals("NULL", paramInfo.getParamName());
        Assert.assertEquals("NULL", paramInfo.getFormatedName());
    }
}
