package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface BlobHandler {
	/** 最小大小 */
	int minSize() default 0;

	/** 最大大小 */
	int maxSize() default 0;
}
