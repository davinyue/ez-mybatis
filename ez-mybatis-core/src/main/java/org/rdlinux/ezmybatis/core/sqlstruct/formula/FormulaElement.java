package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.FormulaOperator;

/**
 * 公示要素, 参数公示运算的成员
 */
public interface FormulaElement extends SqlStruct {
    FormulaOperator getOperator();
}
