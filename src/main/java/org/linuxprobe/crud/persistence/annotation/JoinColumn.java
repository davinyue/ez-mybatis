package org.linuxprobe.crud.persistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 连接查询指定Join列注解 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JoinColumn {
	/** 主表链接字段效果等同于principal */
	String value() default "";

	/** 主表链接字段效果等同于value */
	String principal() default "";

	/** 从表链接字段 */
	String subordinate() default "";
}
