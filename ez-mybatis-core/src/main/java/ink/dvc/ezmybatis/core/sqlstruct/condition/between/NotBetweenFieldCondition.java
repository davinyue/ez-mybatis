package ink.dvc.ezmybatis.core.sqlstruct.condition.between;

import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * not between 条件
 */
public class NotBetweenFieldCondition extends BetweenFieldCondition {


    public NotBetweenFieldCondition(LoginSymbol loginSymbol, EntityTable table, String field, Object minValue,
                                    Object maxValue) {
        super(loginSymbol, table, field, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
