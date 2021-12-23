package ink.dvc.ezmybatis.core.sqlstruct.condition;

import ink.dvc.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.NotBetweenColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.NotBetweenFieldCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNotNullColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNotNullFiledCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNullColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNullFieldCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

public abstract class ConditionBuilder<ParentBuilder, SonBuilder> {
    protected ParentBuilder parentBuilder;
    protected SonBuilder sonBuilder = null;
    protected List<Condition> conditions;
    protected Table table;

    public ConditionBuilder(ParentBuilder parentBuilder, List<Condition> conditions, Table table) {
        this.parentBuilder = parentBuilder;
        this.conditions = conditions;
        this.table = table;
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }

    private void checkEntityTable() {
        if (!(this.table instanceof EntityTable)) {
            throw new IllegalArgumentException("Only EntityTable is supported");
        }
    }

    public SonBuilder addFieldCondition(Condition.LoginSymbol loginSymbol, String field,
                                        Operator operator, Object value) {
        this.checkEntityTable();
        this.conditions.add(new NormalFieldCondition(loginSymbol, (EntityTable) this.table, field, operator,
                value));
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                        Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(loginSymbol, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(Condition.LoginSymbol loginSymbol, String column,
                                         Operator operator, Object value) {
        this.conditions.add(new NormalColumnCondition(loginSymbol, this.table, column, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                         Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(loginSymbol, column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(Condition.LoginSymbol loginSymbol, String field,
                                        Object value) {
        return this.addFieldCondition(loginSymbol, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                        Object value) {
        if (sure) {
            return this.addFieldCondition(loginSymbol, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(Condition.LoginSymbol loginSymbol, String column,
                                         Object value) {
        return this.addColumnCondition(loginSymbol, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                         Object value) {
        if (sure) {
            return this.addColumnCondition(loginSymbol, column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(String field, Operator operator, Object value) {
        return this.addFieldCondition(Condition.LoginSymbol.AND, field, operator, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Operator operator, Object value) {
        return this.addColumnCondition(Condition.LoginSymbol.AND, column, operator, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(column, operator, value);
        }
        return this.sonBuilder;
    }


    public SonBuilder addFieldCondition(String field, Object value) {
        return this.addFieldCondition(Condition.LoginSymbol.AND, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Object value) {
        if (sure) {
            return this.addFieldCondition(field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Object value) {
        return this.addColumnCondition(Condition.LoginSymbol.AND, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Object value) {
        if (sure) {
            return this.addColumnCondition(column, value);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(Condition.LoginSymbol loginSymbol, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(loginSymbol, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, Condition.LoginSymbol loginSymbol, String field) {
        if (sure) {
            return this.addFieldIsNullCondition(loginSymbol, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(Condition.LoginSymbol loginSymbol, String column) {
        this.conditions.add(new IsNullColumnCondition(loginSymbol, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, Condition.LoginSymbol loginSymbol, String column) {
        if (sure) {
            return this.addColumnIsNullCondition(loginSymbol, column);
        }
        return this.sonBuilder;
    }


    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, String field) {
        if (sure) {
            return this.addFieldIsNullCondition(field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(String column) {
        this.conditions.add(new IsNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, String column) {
        if (sure) {
            return this.addColumnIsNullCondition(column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(Condition.LoginSymbol loginSymbol, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(loginSymbol, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                 String field) {
        if (sure) {
            return this.addFieldIsNotNullCondition(loginSymbol, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(Condition.LoginSymbol loginSymbol, String column) {
        this.conditions.add(new IsNotNullColumnCondition(loginSymbol, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                  String column) {
        if (sure) {
            return this.addColumnIsNotNullCondition(loginSymbol, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(Condition.LoginSymbol.AND, (EntityTable) this.table,
                field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, String field) {
        if (sure) {
            return this.addFieldIsNotNullCondition(field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNullCondition(String column) {
        this.conditions.add(new IsNotNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, String column) {
        if (sure) {
            return this.addColumnIsNotNullCondition(column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(Condition.LoginSymbol loginSymbol, String field,
                                          Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(loginSymbol, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                          Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldBtCondition(loginSymbol, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(Condition.LoginSymbol loginSymbol, String column,
                                           Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(loginSymbol, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                           String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnBtCondition(loginSymbol, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldBtCondition(field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(String column, Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, String column, Object minValue,
                                           Object maxValue) {
        if (sure) {
            return this.addColumnBtCondition(column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(Condition.LoginSymbol loginSymbol, String field,
                                             Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(loginSymbol, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                             Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldNotBtCondition(loginSymbol, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Condition.LoginSymbol loginSymbol, String column,
                                              Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(loginSymbol, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                              String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnNotBtCondition(loginSymbol, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldNotBtCondition(field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(String column, Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, String column, Object minValue,
                                              Object maxValue) {
        if (sure) {
            return this.addColumnNotBtCondition(column, minValue, maxValue);
        }
        return this.sonBuilder;
    }
}
