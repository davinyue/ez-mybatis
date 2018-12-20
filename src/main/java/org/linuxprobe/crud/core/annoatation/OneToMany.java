package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface OneToMany {
	/** 指定主表用哪个列的值去查询从表,效果等同于principal */
	String value() default "";

	/** 指定主表用哪个列的值去查询从表,效果等同于value */
	String principal() default "";

	/** 指定从表使用哪个字段作为查询条件 */
	String subordinate() default "";
}
