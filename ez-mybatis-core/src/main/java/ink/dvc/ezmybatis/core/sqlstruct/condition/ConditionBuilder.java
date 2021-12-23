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

    public SonBuilder add(Condition.LoginSymbol loginSymbol, String field,
                          Operator operator, Object value) {
        this.checkEntityTable();
        this.conditions.add(new NormalFieldCondition(loginSymbol, (EntityTable) this.table, field, operator,
                value));
        return this.sonBuilder;
    }

    public SonBuilder add(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                          Operator operator, Object value) {
        if (sure) {
            return this.add(loginSymbol, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumn(Condition.LoginSymbol loginSymbol, String column,
                                Operator operator, Object value) {
        this.conditions.add(new NormalColumnCondition(loginSymbol, this.table, column, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addColumn(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                Operator operator, Object value) {
        if (sure) {
            return this.addColumn(loginSymbol, column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder add(Condition.LoginSymbol loginSymbol, String field,
                          Object value) {
        return this.add(loginSymbol, field, Operator.eq, value);
    }

    public SonBuilder add(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                          Object value) {
        if (sure) {
            return this.add(loginSymbol, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumn(Condition.LoginSymbol loginSymbol, String column,
                                Object value) {
        return this.addColumn(loginSymbol, column, Operator.eq, value);
    }

    public SonBuilder addColumn(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                Object value) {
        if (sure) {
            return this.addColumn(loginSymbol, column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder add(String field, Operator operator, Object value) {
        return this.add(Condition.LoginSymbol.AND, field, operator, value);
    }

    public SonBuilder add(boolean sure, String field, Operator operator, Object value) {
        if (sure) {
            return this.add(field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumn(String column, Operator operator, Object value) {
        return this.addColumn(Condition.LoginSymbol.AND, column, operator, value);
    }

    public SonBuilder addColumn(boolean sure, String column, Operator operator, Object value) {
        if (sure) {
            return this.addColumn(column, operator, value);
        }
        return this.sonBuilder;
    }


    public SonBuilder add(String field, Object value) {
        return this.add(Condition.LoginSymbol.AND, field, Operator.eq, value);
    }

    public SonBuilder add(boolean sure, String field, Object value) {
        if (sure) {
            return this.add(field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumn(String column, Object value) {
        return this.addColumn(Condition.LoginSymbol.AND, column, Operator.eq, value);
    }

    public SonBuilder addColumn(boolean sure, String column, Object value) {
        if (sure) {
            return this.addColumn(column, value);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNull(Condition.LoginSymbol loginSymbol, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(loginSymbol, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNull(boolean sure, Condition.LoginSymbol loginSymbol, String field) {
        if (sure) {
            return this.addIsNull(loginSymbol, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNull(Condition.LoginSymbol loginSymbol, String column) {
        this.conditions.add(new IsNullColumnCondition(loginSymbol, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                      String column) {
        if (sure) {
            return this.addColumnIsNull(loginSymbol, column);
        }
        return this.sonBuilder;
    }


    /**
     * 添加is null条件
     */
    public SonBuilder addIsNull(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNull(boolean sure, String field) {
        if (sure) {
            return this.addIsNull(field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNull(String column) {
        this.conditions.add(new IsNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNull(boolean sure, String column) {
        if (sure) {
            return this.addColumnIsNull(column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNull(Condition.LoginSymbol loginSymbol, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(loginSymbol, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                   String field) {
        if (sure) {
            return this.addIsNotNull(loginSymbol, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNull(Condition.LoginSymbol loginSymbol, String column) {
        this.conditions.add(new IsNotNullColumnCondition(loginSymbol, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                         String column) {
        if (sure) {
            return this.addColumnIsNotNull(loginSymbol, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNull(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(Condition.LoginSymbol.AND, (EntityTable) this.table,
                field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNull(boolean sure, String field) {
        if (sure) {
            return this.addIsNotNull(field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNull(String column) {
        this.conditions.add(new IsNotNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNull(boolean sure, String column) {
        if (sure) {
            return this.addColumnIsNotNull(column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addBt(Condition.LoginSymbol loginSymbol, String field,
                            Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(loginSymbol, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addBt(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                            Object minValue, Object maxValue) {
        if (sure) {
            return this.addBt(loginSymbol, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBt(Condition.LoginSymbol loginSymbol, String column,
                                  Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(loginSymbol, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBt(boolean sure, Condition.LoginSymbol loginSymbol,
                                  String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnBt(loginSymbol, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addBt(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addBt(boolean sure, String field, Object minValue, Object maxValue) {
        if (sure) {
            return this.addBt(field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBt(String column, Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBt(boolean sure, String column, Object minValue,
                                  Object maxValue) {
        if (sure) {
            return this.addColumnBt(column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addNotBt(Condition.LoginSymbol loginSymbol, String field,
                               Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(loginSymbol, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addNotBt(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                               Object minValue, Object maxValue) {
        if (sure) {
            return this.addNotBt(loginSymbol, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBt(Condition.LoginSymbol loginSymbol, String column,
                                     Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(loginSymbol, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBt(boolean sure, Condition.LoginSymbol loginSymbol,
                                     String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnNotBt(loginSymbol, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addNotBt(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(Condition.LoginSymbol.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addNotBt(boolean sure, String field, Object minValue, Object maxValue) {
        if (sure) {
            return this.addNotBt(field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBt(String column, Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBt(boolean sure, String column, Object minValue,
                                     Object maxValue) {
        if (sure) {
            return this.addColumnNotBt(column, minValue, maxValue);
        }
        return this.sonBuilder;
    }
}
