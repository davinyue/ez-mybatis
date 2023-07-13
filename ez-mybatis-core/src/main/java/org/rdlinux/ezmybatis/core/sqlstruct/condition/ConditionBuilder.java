package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.ColumnCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FieldCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullFiledCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

/**
 * @param <ParentBuilder> 上级构造器, 调用.done时将返回上级构造器
 * @param <SonBuilder>    本级构造器, 一般定义为继承类
 */
public abstract class ConditionBuilder<ParentBuilder, SonBuilder> {
    protected ParentBuilder parentBuilder;
    protected SonBuilder sonBuilder = null;
    protected List<Condition> conditions;
    protected Table table;
    protected Table otherTable;

    public ConditionBuilder(ParentBuilder parentBuilder, List<Condition> conditions, Table table, Table otherTable) {
        this.parentBuilder = parentBuilder;
        this.conditions = conditions;
        this.table = table;
        this.otherTable = otherTable;
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }

    protected void checkEntityTable() {
        if (!(this.table instanceof EntityTable)) {
            throw new IllegalArgumentException("Only EntityTable is supported");
        }
    }

    private void checkOtherEntityTable() {
        if (!(this.otherTable instanceof EntityTable)) {
            throw new IllegalArgumentException("Only EntityTable is supported");
        }
    }

    private void checkAllEntityTable() {
        this.checkEntityTable();
        this.checkOtherEntityTable();
    }

    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, String field,
                                        Operator operator, Object value) {
        this.checkEntityTable();
        this.conditions.add(new NormalFieldCondition(logicalOperator, (EntityTable) this.table, field, operator,
                value));
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(EntityTable table, LogicalOperator logicalOperator, String field,
                                        Operator operator, Object value) {
        this.conditions.add(new NormalFieldCondition(logicalOperator, table, field, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                        Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(logicalOperator, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator, String field,
                                        Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(table, logicalOperator, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        this.conditions.add(new NormalColumnCondition(logicalOperator, this.table, column, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(Table table, LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        this.conditions.add(new NormalColumnCondition(logicalOperator, table, column, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(logicalOperator, column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, Table table, LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(table, logicalOperator, column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, String field,
                                        Object value) {
        return this.addFieldCondition(logicalOperator, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(EntityTable table, LogicalOperator logicalOperator, String field,
                                        Object value) {
        return this.addFieldCondition(table, logicalOperator, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                        Object value) {
        if (sure) {
            return this.addFieldCondition(logicalOperator, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator, String field,
                                        Object value) {
        if (sure) {
            return this.addFieldCondition(table, logicalOperator, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column,
                                         Object value) {
        return this.addColumnCondition(logicalOperator, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(Table table, LogicalOperator logicalOperator, String column,
                                         Object value) {
        return this.addColumnCondition(table, logicalOperator, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column,
                                         Object value) {
        if (sure) {
            return this.addColumnCondition(logicalOperator, column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, Table table, LogicalOperator logicalOperator, String column,
                                         Object value) {
        if (sure) {
            return this.addColumnCondition(table, logicalOperator, column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(String field, Operator operator, Object value) {
        return this.addFieldCondition(LogicalOperator.AND, field, operator, value);
    }

    public SonBuilder addFieldCondition(EntityTable table, String field, Operator operator, Object value) {
        return this.addFieldCondition(table, LogicalOperator.AND, field, operator, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Operator operator,
                                        Object value) {
        if (sure) {
            return this.addFieldCondition(table, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Operator operator, Object value) {
        return this.addColumnCondition(LogicalOperator.AND, column, operator, value);
    }

    public SonBuilder addColumnCondition(Table table, String column, Operator operator, Object value) {
        return this.addColumnCondition(table, LogicalOperator.AND, column, operator, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(table, column, operator, value);
        }
        return this.sonBuilder;
    }


    public SonBuilder addFieldCondition(String field, Object value) {
        return this.addFieldCondition(LogicalOperator.AND, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(EntityTable table, String field, Object value) {
        return this.addFieldCondition(table, LogicalOperator.AND, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Object value) {
        if (sure) {
            return this.addFieldCondition(field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Object value) {
        if (sure) {
            return this.addFieldCondition(table, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Object value) {
        return this.addColumnCondition(LogicalOperator.AND, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(Table table, String column, Object value) {
        return this.addColumnCondition(table, LogicalOperator.AND, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Object value) {
        if (sure) {
            return this.addColumnCondition(column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Object value) {
        if (sure) {
            return this.addColumnCondition(table, column, value);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(logicalOperator, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(EntityTable table, LogicalOperator logicalOperator, String field) {
        this.conditions.add(new IsNullFieldCondition(logicalOperator, table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, LogicalOperator logicalOperator, String field) {
        if (sure) {
            return this.addFieldIsNullCondition(logicalOperator, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator,
                                              String field) {
        if (sure) {
            return this.addFieldIsNullCondition(table, logicalOperator, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNullColumnCondition(logicalOperator, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(Table table, LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNullColumnCondition(logicalOperator, table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, LogicalOperator logicalOperator, String column) {
        if (sure) {
            return this.addColumnIsNullCondition(logicalOperator, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, Table table, LogicalOperator logicalOperator,
                                               String column) {
        if (sure) {
            return this.addColumnIsNullCondition(table, logicalOperator, column);
        }
        return this.sonBuilder;
    }


    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addFieldIsNullCondition(EntityTable table, String field) {
        this.conditions.add(new IsNullFieldCondition(LogicalOperator.AND, table, field));
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
    public SonBuilder addFieldIsNullCondition(boolean sure, EntityTable table, String field) {
        if (sure) {
            return this.addFieldIsNullCondition(table, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(String column) {
        this.conditions.add(new IsNullColumnCondition(LogicalOperator.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(Table table, String column) {
        this.conditions.add(new IsNullColumnCondition(LogicalOperator.AND, table, column));
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
     * 添加is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, Table table, String column) {
        if (sure) {
            return this.addColumnIsNullCondition(table, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(logicalOperator, (EntityTable) this.table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(EntityTable table, LogicalOperator logicalOperator, String field) {
        this.conditions.add(new IsNotNullFiledCondition(logicalOperator, table, field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                 String field) {
        if (sure) {
            return this.addFieldIsNotNullCondition(logicalOperator, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator,
                                                 String field) {
        if (sure) {
            return this.addFieldIsNotNullCondition(table, logicalOperator, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNotNullColumnCondition(logicalOperator, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(Table table, LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNotNullColumnCondition(logicalOperator, table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                  String column) {
        if (sure) {
            return this.addColumnIsNotNullCondition(logicalOperator, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, Table table, LogicalOperator logicalOperator,
                                                  String column) {
        if (sure) {
            return this.addColumnIsNotNullCondition(table, logicalOperator, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(LogicalOperator.AND, (EntityTable) this.table,
                field));
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(EntityTable table, String field) {
        this.conditions.add(new IsNotNullFiledCondition(LogicalOperator.AND, table, field));
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
     * 添加is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, EntityTable table, String field) {
        if (sure) {
            return this.addFieldIsNotNullCondition(table, field);
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNullCondition(String column) {
        this.conditions.add(new IsNotNullColumnCondition(LogicalOperator.AND, this.table, column));
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNullCondition(Table table, String column) {
        this.conditions.add(new IsNotNullColumnCondition(LogicalOperator.AND, table, column));
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
     * 添加is null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, Table table, String column) {
        if (sure) {
            return this.addColumnIsNotNullCondition(table, column);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(LogicalOperator logicalOperator, String field,
                                          Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(logicalOperator, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(EntityTable table, LogicalOperator logicalOperator, String field,
                                          Object minValue, Object maxValue) {
        this.conditions.add(new BetweenFieldCondition(logicalOperator, table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                          Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldBtCondition(logicalOperator, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator, String field,
                                          Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldBtCondition(table, logicalOperator, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(LogicalOperator logicalOperator, String column,
                                           Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(logicalOperator, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(Table table, LogicalOperator logicalOperator, String column,
                                           Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(logicalOperator, table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, LogicalOperator logicalOperator,
                                           String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnBtCondition(logicalOperator, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Table table, LogicalOperator logicalOperator,
                                           String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnBtCondition(table, logicalOperator, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addFieldBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        this.conditions.add(new BetweenFieldCondition(LogicalOperator.AND, table, field, minValue, maxValue));
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
    public SonBuilder addFieldBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                          Object maxValue) {
        if (sure) {
            return this.addFieldBtCondition(table, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(String column, Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(LogicalOperator.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(Table table, String column, Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(LogicalOperator.AND, table, column, minValue, maxValue));
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
     * 添加between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Table table, String column, Object minValue,
                                           Object maxValue) {
        if (sure) {
            return this.addColumnBtCondition(table, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(LogicalOperator logicalOperator, String field,
                                             Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(logicalOperator, (EntityTable) this.table, field, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(EntityTable table, LogicalOperator logicalOperator, String field,
                                             Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenFieldCondition(logicalOperator, table, field, minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                             Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldNotBtCondition(logicalOperator, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, EntityTable table, LogicalOperator logicalOperator,
                                             String field, Object minValue, Object maxValue) {
        if (sure) {
            return this.addFieldNotBtCondition(table, logicalOperator, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(LogicalOperator logicalOperator, String column,
                                              Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(logicalOperator, this.table, column, minValue,
                maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Table table, LogicalOperator logicalOperator, String column,
                                              Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(logicalOperator, table, column, minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, LogicalOperator logicalOperator,
                                              String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnNotBtCondition(logicalOperator, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Table table, LogicalOperator logicalOperator,
                                              String column, Object minValue, Object maxValue) {
        if (sure) {
            return this.addColumnNotBtCondition(table, logicalOperator, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addFieldNotBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenFieldCondition(LogicalOperator.AND, table, field, minValue, maxValue));
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
    public SonBuilder addFieldNotBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                             Object maxValue) {
        if (sure) {
            return this.addFieldNotBtCondition(table, field, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(String column, Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(LogicalOperator.AND, this.table, column,
                minValue, maxValue));
        return this.sonBuilder;
    }

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Table table, String column, Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(LogicalOperator.AND, table, column, minValue, maxValue));
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

    /**
     * 添加not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Table table, String column, Object minValue,
                                              Object maxValue) {
        if (sure) {
            return this.addColumnNotBtCondition(table, column, minValue, maxValue);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        if (sure) {
            this.conditions.add(new FieldCompareCondition(logicalOperator, leftTable, leftField, operator, rightTable,
                    rightField));
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addFieldCompareCondition(sure, LogicalOperator.AND, leftTable, leftField, operator,
                rightTable, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addFieldCompareCondition(true, logicalOperator, leftTable, leftField, operator,
                rightTable, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                               EntityTable rightTable, String rightField) {
        return this.addFieldCompareCondition(true, LogicalOperator.AND, leftTable, leftField, operator,
                rightTable, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, String leftField, Operator operator,
                                               String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, logicalOperator, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, Operator operator, String rightField) {
        return this.addFieldCompareCondition(LogicalOperator.AND, leftField, operator, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, String rightField) {
        return this.addFieldCompareCondition(leftField, Operator.eq, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, String rightField) {
        if (sure) {
            return this.addFieldCompareCondition(leftField, rightField);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, String leftField, String rightField) {
        return this.addFieldCompareCondition(logicalOperator, leftField, Operator.eq, rightField);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftField,
                                               String rightField) {
        if (sure) {
            return this.addFieldCompareCondition(logicalOperator, leftField, rightField);
        }
        return this.sonBuilder;
    }


    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, Operator operator, String rightField) {
        if (sure) {
            return this.addFieldCompareCondition(leftField, operator, rightField);
        }
        return this.sonBuilder;
    }


    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftField,
                                               Operator operator, String rightField) {
        if (sure) {
            return this.addFieldCompareCondition(logicalOperator, leftField, operator, rightField);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, Table leftTable,
                                                String leftColumn, Operator operator,
                                                Table rightTable, String rightColumn) {
        if (sure) {
            this.conditions.add(new ColumnCompareCondition(logicalOperator, leftTable, leftColumn, operator,
                    rightTable, rightColumn));
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, Table leftTable, String leftColumn,
                                                Operator operator,
                                                Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(true, logicalOperator, leftTable, leftColumn, operator, rightTable,
                rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, Table leftTable, String leftColumn, Operator operator,
                                                Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(sure, LogicalOperator.AND, leftTable, leftColumn, operator,
                rightTable, rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(Table leftTable, String leftColumn, Operator operator,
                                                Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(true, LogicalOperator.AND, leftTable, leftColumn, operator,
                rightTable, rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, String leftColumn, Operator operator,
                                                String rightColumn) {
        return this.addColumnCompareCondition(logicalOperator, this.table, leftColumn, operator, this.otherTable,
                rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftColumn,
                                                Operator operator, String rightColumn) {
        if (sure) {
            return this.addColumnCompareCondition(logicalOperator, leftColumn, operator, rightColumn);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(LogicalOperator.AND, leftColumn, operator, rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, Operator operator,
                                                String rightColumn) {
        if (sure) {
            return this.addColumnCompareCondition(leftColumn, operator, rightColumn);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, String leftColumn,
                                                String rightColumn) {
        return this.addColumnCompareCondition(logicalOperator, leftColumn, Operator.eq, rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftColumn,
                                                String rightColumn) {
        if (sure) {
            return this.addColumnCompareCondition(logicalOperator, leftColumn, rightColumn);
        }
        return this.sonBuilder;
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(LogicalOperator.AND, leftColumn, rightColumn);
    }

    /**
     * 添对比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, String rightColumn) {
        if (sure) {
            return this.addColumnCompareCondition(leftColumn, rightColumn);
        }
        return this.sonBuilder;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(boolean sure, LogicalOperator logicalOperator, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(logicalOperator, sql));
        }
        return this.sonBuilder;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(LogicalOperator logicalOperator, String sql) {
        return this.addSqlCondition(true, logicalOperator, sql);
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(boolean sure, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(LogicalOperator.AND, sql));
        }
        return this.sonBuilder;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(String sql) {
        return this.addSqlCondition(true, sql);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(boolean sure, LogicalOperator logicalOperator, Function function,
                                                   Operator operator, Object value) {
        if (sure) {
            this.conditions.add(new FunctionCompareValueCondition(logicalOperator, function, operator, value));
        }
        return this.sonBuilder;
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(LogicalOperator logicalOperator, Function function,
                                                   Operator operator, Object value) {
        return this.addFuncCompareValueCondition(true, logicalOperator, function, operator, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(boolean sure, LogicalOperator logicalOperator, Function function,
                                                   Object value) {
        return this.addFuncCompareValueCondition(sure, logicalOperator, function, Operator.eq, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(LogicalOperator logicalOperator, Function function,
                                                   Object value) {
        return this.addFuncCompareValueCondition(true, logicalOperator, function, Operator.eq, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(boolean sure, Function function, Operator operator, Object value) {
        return this.addFuncCompareValueCondition(sure, LogicalOperator.AND, function, operator, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(Function function, Operator operator, Object value) {
        return this.addFuncCompareValueCondition(true, LogicalOperator.AND, function, operator, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(boolean sure, Function function, Object value) {
        return this.addFuncCompareValueCondition(sure, LogicalOperator.AND, function, Operator.eq, value);
    }

    /**
     * 添函数对比值条件
     */
    public SonBuilder addFuncCompareValueCondition(Function function, Object value) {
        return this.addFuncCompareValueCondition(true, LogicalOperator.AND, function, Operator.eq, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(boolean sure, LogicalOperator logicalOperator, Formula formula,
                                                      Operator operator, Object value) {
        if (sure) {
            this.conditions.add(new FormulaCompareValueCondition(logicalOperator, formula, operator, value));
        }
        return this.sonBuilder;
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(LogicalOperator logicalOperator, Formula formula,
                                                      Operator operator, Object value) {
        return this.addFormulaCompareValueCondition(true, logicalOperator, formula, operator, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(boolean sure, LogicalOperator logicalOperator, Formula formula,
                                                      Object value) {
        return this.addFormulaCompareValueCondition(sure, logicalOperator, formula, Operator.eq, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(LogicalOperator logicalOperator, Formula formula,
                                                      Object value) {
        return this.addFormulaCompareValueCondition(true, logicalOperator, formula, Operator.eq, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(boolean sure, Formula formula, Operator operator, Object value) {
        return this.addFormulaCompareValueCondition(sure, LogicalOperator.AND, formula, operator, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(Formula formula, Operator operator, Object value) {
        return this.addFormulaCompareValueCondition(true, LogicalOperator.AND, formula, operator, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(boolean sure, Formula formula, Object value) {
        return this.addFormulaCompareValueCondition(sure, LogicalOperator.AND, formula, Operator.eq, value);
    }

    /**
     * 添公式对比值条件
     */
    public SonBuilder addFormulaCompareValueCondition(Formula formula, Object value) {
        return this.addFormulaCompareValueCondition(true, LogicalOperator.AND, formula, Operator.eq, value);
    }
}
