package org.linuxprobe.crud.core.annoatation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** 更新时忽略该字段，Ignore this field when updating */
@Retention(RUNTIME)
@Target(FIELD)
public @interface UpdateIgnore {

}
