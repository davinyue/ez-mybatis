package org.rdlinux.ezmybatis.annotation;

import java.lang.annotation.*;


/**
 * 该注解用于标注mybatis sql生成器的方法名, 作用是快速对比与查找方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlProviderMethod {
    String value();
}
