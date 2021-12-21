package ink.dvc.ezmybatis.core.sqlstruct.condition.between;

import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

public class NotBetweenColumnCondition extends BetweenColumnCondition {
    public NotBetweenColumnCondition(LoginSymbol loginSymbol, EntityTable table, String column, Object minValue,
                                     Object maxValue) {
        super(loginSymbol, table, column, minValue, maxValue);
        this.operator = Operator.notBetween;
    }
}
