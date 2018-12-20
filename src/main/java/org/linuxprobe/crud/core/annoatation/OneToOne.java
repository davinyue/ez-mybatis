package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface OneToOne {
	/**
	 * 可以指定关联列的名称，默认是field名称驼峰转下划线后加‘_id’ 例如: orgName-->org_name_id
	 */
	/**
	 * you can specify the name of the associated column, the default is after the
	 * Field Name hump turn underline add '_id', eg: org -> org_id
	 */
	String value() default "";
}
