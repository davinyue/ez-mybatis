package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** boolean save type handle */
@Retention(RUNTIME)
@Target(FIELD)
public @interface BooleanHandler {
	BooleanCustomerType value() default BooleanCustomerType.ToInt;

	public static enum BooleanCustomerType {
		/** save as int 1 or int 0  */
		ToInt,
		/** save as yes or no sring*/
		YesAndNo,
		/** save as true or false string */
		TrueAndFalse;
	}
}
