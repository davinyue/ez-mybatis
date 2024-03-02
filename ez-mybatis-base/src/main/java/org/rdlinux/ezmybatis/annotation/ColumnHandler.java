package org.rdlinux.ezmybatis.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnHandler {
    /**
     * 指定类需要继承自{@link org.apache.ibatis.type.TypeHandler}
     */
    Class<?> value();
}
