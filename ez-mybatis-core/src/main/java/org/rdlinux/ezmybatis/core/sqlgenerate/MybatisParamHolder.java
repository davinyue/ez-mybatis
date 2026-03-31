package org.rdlinux.ezmybatis.core.sqlgenerate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.FieldAccessScope;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.interceptor.executor.TypeHandlerInitLogic;
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
     * 原始mybatis参数映射
     */
    private final Map<String, Object> mybatisParam;
    private final Configuration configuration;
    /**
     * 当前数组
     */
    private ArrayList<Object> currentArray;
    /**
     * 当前数组名称下标
     */
    private int currentArrayIndex = -1;

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
     * 获取一个参数名称，以 {@link FieldAccessScope#DSL_CONDITION} 域触发字段访问监听器。
     * <p>适用于 EzQuery / EzUpdate / EzDelete 等 DSL 构造中用户手动绑定的字段值，
     * 典型用途包括：将 where 条件中的明文值加密后与数据库密文进行比对，
     * 以及 EzUpdate 中对指定字段设置值时的加密处理。</p>
     *
     * @param modelType  实体类型
     * @param field      被访问的属性
     * @param paramValue 参数值
     * @return MyBatis 参数占位符表达式
     */
    public String getMybatisParamName(Class<?> modelType, Field field, Object paramValue) {
        if (this.configuration != null && modelType != null && field != null) {
            paramValue = EzMybatisContent.onBuildSqlGetField(this.configuration, FieldAccessScope.DSL_CONDITION,
                    modelType, field, paramValue);
        }
        return this.getMybatisParamNameWithRegisterTypeHandler(modelType, field, paramValue);
    }

    /**
     * 获取一个参数名称，以 {@link FieldAccessScope#ENTITY_PERSIST} 域触发字段访问监听器。
     * <p>适用于整体实体持久化场景（insert / update / replace / batchInsert 等），
     * 实体的全部或非 null 字段被遍历写入数据库时调用，典型用途为对持久化字段做统一加密。</p>
     *
     * @param modelType  实体类型
     * @param field      被访问的属性
     * @param paramValue 参数值
     * @return MyBatis 参数占位符表达式
     */
    public String entityPersistGetMybatisParamName(Class<?> modelType, Field field, Object paramValue) {
        if (this.configuration != null && modelType != null && field != null) {
            paramValue = EzMybatisContent.onBuildSqlGetField(this.configuration, FieldAccessScope.ENTITY_PERSIST,
                    modelType, field,
                    paramValue);
        }
        return this.getMybatisParamNameWithRegisterTypeHandler(modelType, field, paramValue);
    }

    /**
     * 获取mybatis 参数名称, 并且注册类型处理器
     */
    private String getMybatisParamNameWithRegisterTypeHandler(Class<?> modelType, Field field, Object paramValue) {
        MybatisParamInfo mybatisParamInfo = this.getMybatisParamName(paramValue);
        String formatedName = mybatisParamInfo.getFormatedName();
        if (this.configuration != null && modelType != null && field != null) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(this.configuration, modelType);
            EntityFieldInfo entityFieldInfo = entityClassInfo.getFieldInfo(field.getName());
            if (entityFieldInfo != null) {
                TypeHandler<?> typeHandler = entityFieldInfo.getTypeHandler();
                if (typeHandler != null) {
                    TypeHandlerInitLogic.registerTypeHandler(mybatisParamInfo.getParamName(), typeHandler);
                }
            }
        }
        return formatedName;
    }

    /**
     * 获取一个参数名称
     *
     * @param paramValue 参数值
     */
    public MybatisParamInfo getMybatisParamName(Object paramValue) {
        if (paramValue == null) {
            return new MybatisParamInfo()
                    .setParamName("NULL")
                    .setFormatedName("NULL");
        }
        if (this.currentArray.size() >= ARRAY_MAX_PARAM) {
            this.transposeArray();
        }
        this.currentArray.add(paramValue);
        String escape = getEscapeChar(paramValue);
        String mybatisParamName = this.getCurrentHashKeyName() + "[" + (this.currentArray.size() - 1) + "]";
        return new MybatisParamInfo()
                .setParamName(mybatisParamName)
                .setFormatedName(escape + "{" + mybatisParamName + "}");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class MybatisParamInfo {
        private String formatedName;
        private String paramName;
    }
}
