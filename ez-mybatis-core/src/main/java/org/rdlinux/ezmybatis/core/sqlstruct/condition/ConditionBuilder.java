package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.ColumnCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FieldCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullFiledCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

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

    private void checkEntityTable() {
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

    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                        Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(logicalOperator, field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        this.conditions.add(new NormalColumnCondition(logicalOperator, this.table, column, operator, value));
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column,
                                         Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(logicalOperator, column, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, String field,
                                        Object value) {
        return this.addFieldCondition(logicalOperator, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field,
                                        Object value) {
        if (sure) {
            return this.addFieldCondition(logicalOperator, field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column,
                                         Object value) {
        return this.addColumnCondition(logicalOperator, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column,
                                         Object value) {
        if (sure) {
            return this.addColumnCondition(logicalOperator, column, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addFieldCondition(String field, Operator operator, Object value) {
        return this.addFieldCondition(LogicalOperator.AND, field, operator, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Object value) {
        if (sure) {
            return this.addFieldCondition(field, operator, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Operator operator, Object value) {
        return this.addColumnCondition(LogicalOperator.AND, column, operator, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Object value) {
        if (sure) {
            return this.addColumnCondition(column, operator, value);
        }
        return this.sonBuilder;
    }


    public SonBuilder addFieldCondition(String field, Object value) {
        return this.addFieldCondition(LogicalOperator.AND, field, Operator.eq, value);
    }

    public SonBuilder addFieldCondition(boolean sure, String field, Object value) {
        if (sure) {
            return this.addFieldCondition(field, value);
        }
        return this.sonBuilder;
    }

    public SonBuilder addColumnCondition(String column, Object value) {
        return this.addColumnCondition(LogicalOperator.AND, column, Operator.eq, value);
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
    public SonBuilder addFieldIsNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(logicalOperator, (EntityTable) this.table, field));
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
    public SonBuilder addColumnIsNullCondition(LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNullColumnCondition(logicalOperator, this.table, column));
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
    public SonBuilder addFieldIsNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNullFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field));
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
        this.conditions.add(new IsNullColumnCondition(LogicalOperator.AND, this.table, column));
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
    public SonBuilder addFieldIsNotNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(logicalOperator, (EntityTable) this.table, field));
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
    public SonBuilder addColumnIsNotNullCondition(LogicalOperator logicalOperator, String column) {
        this.conditions.add(new IsNotNullColumnCondition(logicalOperator, this.table, column));
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
    public SonBuilder addFieldIsNotNullCondition(String field) {
        this.checkEntityTable();
        this.conditions.add(new IsNotNullFiledCondition(LogicalOperator.AND, (EntityTable) this.table,
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
        this.conditions.add(new IsNotNullColumnCondition(LogicalOperator.AND, this.table, column));
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
    public SonBuilder addColumnBtCondition(LogicalOperator logicalOperator, String column,
                                           Object minValue, Object maxValue) {
        this.conditions.add(new BetweenColumnCondition(logicalOperator, this.table, column, minValue,
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
    public SonBuilder addFieldBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new BetweenFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field,
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
        this.conditions.add(new BetweenColumnCondition(LogicalOperator.AND, this.table, column,
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
    public SonBuilder addColumnNotBtCondition(LogicalOperator logicalOperator, String column,
                                              Object minValue, Object maxValue) {
        this.conditions.add(new NotBetweenColumnCondition(logicalOperator, this.table, column, minValue,
                maxValue));
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
    public SonBuilder addFieldNotBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        this.conditions.add(new NotBetweenFieldCondition(LogicalOperator.AND, (EntityTable) this.table, field,
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
        this.conditions.add(new NotBetweenColumnCondition(LogicalOperator.AND, this.table, column,
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

    /**
     * 添对比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, String leftField, Operator operator,
                                               String rightField) {
        this.checkAllEntityTable();
        this.conditions.add(new FieldCompareCondition(logicalOperator, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField));
        return this.sonBuilder;
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
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, String leftColumn, Operator operator,
                                                String rightColumn) {
        this.conditions.add(new ColumnCompareCondition(logicalOperator, this.table, leftColumn, operator,
                this.otherTable, rightColumn));
        return this.sonBuilder;
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
}
