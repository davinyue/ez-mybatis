package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * case when对比参数
 */
@Getter
public class CaseWhenCompareArgCondition extends CompareArgCondition implements Condition, SqlStruct {
    private CaseWhen caseWhen;


    public CaseWhenCompareArgCondition(LogicalOperator logicalOperator, CaseWhen caseWhen, Operator operator,
                                       Arg value) {
        super(logicalOperator, operator, value);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }

    public CaseWhenCompareArgCondition(LogicalOperator logicalOperator, CaseWhen caseWhen, Operator operator) {
        super(logicalOperator, operator);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }

    public CaseWhenCompareArgCondition(LogicalOperator logicalOperator, CaseWhen caseWhen, Operator operator,
                                       Arg minValue, Arg maxValue) {
        super(logicalOperator, operator, minValue, maxValue);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }

    public CaseWhenCompareArgCondition(LogicalOperator logicalOperator, CaseWhen caseWhen, Operator operator,
                                       List<Arg> values) {
        super(logicalOperator, operator, values);
        Assert.notNull(caseWhen, "caseWhen can not be null");
        this.caseWhen = caseWhen;
    }
}
