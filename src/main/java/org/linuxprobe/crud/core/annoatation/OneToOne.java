package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface OneToOne {
	/**
	 * 可以指定关联field的名称，默认是field名称后加Id 例如: org-->orgId
	 */
	/**
	 * you can specify the name of the associated field, the default is after the
	 * Field Name add 'Id', eg: org -> orgId
	 */
	String value() default "";
}
