package org.rdlinux.ezmybatis.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TypeHandler {
    /**
     * 指定的类型处理器需要继承自{@code org.apache.ibatis.type.TypeHandler}
     */
    Class<?> value();
}
