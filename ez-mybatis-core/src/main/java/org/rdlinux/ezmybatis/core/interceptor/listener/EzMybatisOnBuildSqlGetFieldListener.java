package org.rdlinux.ezmybatis.core.interceptor.listener;

import java.lang.reflect.Field;

/**
 * 对象属性获取监听器, 当构建sql时触发, 通过此接口可以实现加密等功能
 */
public interface EzMybatisOnBuildSqlGetFieldListener {
    /**
     * 当调用get方法时
     *
     * @param ntType 实体对象类型
     * @param field  被获取的属性
     * @param value  获取到的值
     * @return 返回新的设置值
     */
    Object onGet(Class<?> ntType, Field field, Object value);

    /**
     * 执行顺序, 约小越优先
     */
    default int order() {
        return 1;
    }
}
