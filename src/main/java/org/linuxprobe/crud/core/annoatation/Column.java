package org.linuxprobe.crud.core.annoatation;

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

	/** 字段长度，当字段是字符串生效 */
	public int length() default 0;

	/** 超长处理模式，当字段是字符串且设置length大于0时生效 */
	public LengthHandler lengthHandler() default LengthHandler.Sub;

	/** 枚举处理模式，仅当字段是枚举时生效 */
	public EnumHandler enumHandler() default EnumHandler.Ordinal;

	/** 是否不能为空,默认可以为空 */
	public boolean notNull() default false;

	/** 超长处理模式 */
	public static enum LengthHandler {
		/** 减去多余字符 */
		Sub,
		/** 抛出异常 */
		ThrowException
	}

	/** 枚举处理模式 */
	public static enum EnumHandler {
		/** 存序号 */
		Ordinal,
		/** 存名称 */
		Name
	}
}
