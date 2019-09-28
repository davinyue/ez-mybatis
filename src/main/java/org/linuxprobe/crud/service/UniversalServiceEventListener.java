package org.linuxprobe.crud.service;

import java.lang.reflect.Method;

/**
 * 通用service方法调用事件,可用于在操作数据库之前进行一些操作,比如设置审计信息
 */
public interface UniversalServiceEventListener {
    /**
     * 调用前触发
     *
     * @param clazz  调用类
     * @param method 调用方法
     * @param args   方法参数
     */
    default void before(Class clazz, Method method, Object... args) {
    }

    /**
     * 调用后触发
     *
     * @param clazz  调用类
     * @param method 调用方法
     * @param result 执行结果
     * @param args   方法参数
     */
    default void after(Class clazz, Method method, Object result, Object... args) {
    }

}
