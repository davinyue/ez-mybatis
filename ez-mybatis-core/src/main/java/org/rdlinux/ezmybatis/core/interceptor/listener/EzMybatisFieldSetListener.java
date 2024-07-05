package org.rdlinux.ezmybatis.core.interceptor.listener;

/**
 * 对象属性设置监听器, 当执行sql查询出结构组装为对象时调用, 注意如果查询结果返回一个基础类型或者包装类型时, 不支持该事件
 */
public interface EzMybatisFieldSetListener {
    /**
     * 当调用set方法时
     *
     * @param obj   被设置对象
     * @param field 设置属性
     * @param value 设置值
     * @return 返回新的设置值
     */
    Object onSet(Object obj, String field, Object value);

    /**
     * 执行顺序, 约小越优先
     */
    default int order() {
        return 0;
    }
}
