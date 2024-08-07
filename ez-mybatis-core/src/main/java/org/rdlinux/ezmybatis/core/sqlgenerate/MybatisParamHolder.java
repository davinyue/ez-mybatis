package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.utils.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * mybatis 参数持有器, 该类不是线程安全的
 */
public class MybatisParamHolder {
    /**
     * 单个数组最存放参数个数,<br/>
     * 假定未开启指针压缩的情况下, 一个指针占8字节, 假设jvm大对象上线设置为512kb, 则可以存储65536个指针,<br/>
     * 初始容量为10, 扩容21次后容量为47427, 该数字不超过且最接近65536
     */
    private static final int ARRAY_MAX_PARAM = 47427;
    /**
     * 当前数组
     */
    private ArrayList<Object> currentArray;
    /**
     * 当前数组名称下标
     */
    private int currentArrayIndex = -1;
    /**
     * 原始mybatis参数映射
     */
    private Map<String, Object> mybatisParam;
    private Configuration configuration;

    public MybatisParamHolder(Configuration configuration, Map<String, Object> mybatisParam) {
        Assert.notNull(mybatisParam, "mybatisParam can not be null");
        this.configuration = configuration;
        this.mybatisParam = mybatisParam;
        this.transposeArray();
    }

    /**
     * 获取mybatis参数占位符
     */
    private static String getEscapeChar(Object param) {
        if (param instanceof Number) {
            return "$";
        } else {
            return "#";
        }
    }

    /**
     * 转置数组
     */
    private void transposeArray() {
        //设置初始容量为10
        this.currentArray = new ArrayList<>(10);
        this.currentArrayIndex++;
        this.mybatisParam.put(this.getCurrentHashKeyName(), this.currentArray);
    }

    /**
     * 获取当前hash map里面的参数名称
     */
    private String getCurrentHashKeyName() {
        return EzMybatisConstant.MAPPER_PARAM_EZPARAM + "_" + this.currentArrayIndex;
    }

    /**
     * 该方法只能获取mybatis原始map里面的参数, 不支持深度解析
     */
    @SuppressWarnings(value = {"unchecked"})
    public <T> T get(String param) {
        return (T) this.mybatisParam.get(param);
    }

    /**
     * 获取一个参数名称，触发非简单模式事件
     *
     * @param paramValue 参数值
     */
    public String getMybatisParamName(Class<?> modelType, Field field, Object paramValue) {
        if (this.configuration != null && modelType != null && field != null) {
            paramValue = EzMybatisContent.onBuildSqlGetField(this.configuration, Boolean.FALSE, modelType, field,
                    paramValue);
        }
        return this.getMybatisParamName(paramValue);
    }

    /**
     * 获取一个参数名称, 触发简单模式事件
     *
     * @param paramValue 参数值
     */
    public String simpleGetMybatisParamName(Class<?> modelType, Field field, Object paramValue) {
        if (this.configuration != null && modelType != null && field != null) {
            paramValue = EzMybatisContent.onBuildSqlGetField(this.configuration, Boolean.TRUE, modelType, field,
                    paramValue);
        }
        return this.getMybatisParamName(paramValue);
    }

    /**
     * 获取一个参数名称
     *
     * @param paramValue 参数值
     */
    public String getMybatisParamName(Object paramValue) {
        if (paramValue == null) {
            return "NULL";
        }
        if (this.currentArray.size() >= ARRAY_MAX_PARAM) {
            this.transposeArray();
        }
        this.currentArray.add(paramValue);
        String escape = getEscapeChar(paramValue);
        String mybatisParamName = this.getCurrentHashKeyName() + "[" + (this.currentArray.size() - 1) + "]";
        return escape + "{" + mybatisParamName + "}";
    }
}
