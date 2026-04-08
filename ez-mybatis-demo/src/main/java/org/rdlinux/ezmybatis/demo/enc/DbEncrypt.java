package org.rdlinux.ezmybatis.demo.enc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库列加密注解
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface DbEncrypt {
}
