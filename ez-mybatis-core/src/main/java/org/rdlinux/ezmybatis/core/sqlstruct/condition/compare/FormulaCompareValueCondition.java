package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

/**
 * 公式对比值
 */
@Getter
public class FormulaCompareValueCondition implements Condition, SqlStruct {
    private LogicalOperator logicalOperator;
    private Formula formula;
    /**
     * 关系运算符号
     */
    private Operator operator;
    private Object value;

    public FormulaCompareValueCondition(Formula formula, Operator operator, Object value) {
        this(LogicalOperator.AND, formula, operator, value);
    }

    public FormulaCompareValueCondition(LogicalOperator logicalOperator, Formula formula, Operator operator,
                                        Object value) {
        if (operator == null) {
            operator = Operator.eq;
        }
        if (operator == Operator.in || operator == Operator.notIn
                || operator == Operator.isNull || operator == Operator.isNotNull
                || operator == Operator.between || operator == Operator.notBetween) {
            throw new IllegalArgumentException("Unsupported relational operator");
        }
        this.logicalOperator = logicalOperator;
        this.formula = formula;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
