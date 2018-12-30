package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ManyToMany {
	/** 中间表，默认主表表面加下划线加从表表名。例如用户对应有多个角色，用户的表名是user，角色的表面是role，则中间表是user_role */
	String middleTable() default "";

	/** 中间表连接字段，默认为从表表面加下划线加id,例如用户对应有多个角色，用户的表名是user，角色的表面是role，则中间表的连接字段为role_id */
	String joinColumn() default "";

	/** 中间表条件字段,默认为主表表面加下划线加id,例如用户表user为主表，则中间表的条件字段为user_id */
	String conditionColumn() default "";
}
