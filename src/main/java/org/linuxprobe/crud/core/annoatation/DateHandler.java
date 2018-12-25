package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** 时间类型处理 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DateHandler {
	/** 时间保存类型 */
	DateCustomerType customerType() default DateCustomerType.String;

	String pattern() default "yyyy-MM-dd HH:mm:ss";

	public static enum DateCustomerType {
		/** save as pattern matching string value */
		String,
		/** save as unix timestamp */
		Timestamp;
	}
}
