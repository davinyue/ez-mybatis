package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.AndOr;

/**
 * 条件
 */
public interface Condition extends SqlStruct {
    /**
     * 获取逻辑运算符号
     */
    AndOr getAndOr();
}
