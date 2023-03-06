package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;

/**
 * 公示要素, 参数公示运算的成员
 */
public interface FormulaElement extends SqlStruct {
    Operator getOperator();
}
