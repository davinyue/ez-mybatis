package ink.dvc.ezmybatis.core.sqlstruct.condition.nil;

import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 不是空条件
 */
public class IsNotNullColumnCondition extends IsNullColumnCondition {

    public IsNotNullColumnCondition(LoginSymbol loginSymbol, EntityTable table, String column) {
        super(loginSymbol, table, column);
        this.operator = Operator.isNotNull;
    }
}
