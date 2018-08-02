package org.linuxprobe.crud.persistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 列名标注 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	/** 列名 */
	public String value() default "";

	/** 忽略更新,生成的update sql 语句将不包含该字段 */
	public boolean updateIgnore() default false;

	/** 字段长度 */
	public int length() default 0;

	/** 超长处理模式，当length大于0时生效 */
	public LengthHandle lengthHandle() default LengthHandle.Sub;

	/** 超长处理模式 */
	public static enum LengthHandle {
		/** 减去多余字符 */
		Sub,
		/** 抛出异常 */
		ThrowException
	}
}
