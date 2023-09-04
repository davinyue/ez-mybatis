package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;

/**
 * 条件
 */
public interface Condition extends SqlStruct {
    /**
     * 获取逻辑运算符号
     */
    LogicalOperator getLogicalOperator();
}
