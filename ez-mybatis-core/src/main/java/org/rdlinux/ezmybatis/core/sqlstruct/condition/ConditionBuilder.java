package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.*;

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

    protected static List<?> valueToCollection(Object obj) {
        if (obj instanceof Collection) {
            return new ArrayList<>((Collection<?>) obj);
        } else if (obj.getClass().isArray()) {
            return Arrays.asList((Object[]) obj);
        } else {
            return Collections.singletonList(obj);
        }
    }

    protected static Arg valueToArg(Object value) {
        if (value instanceof Arg) {
            return (Arg) value;
        } else if (value instanceof EzQuery) {
            return EzQueryArg.of((EzQuery<?>) value);
        } else {
            return ObjArg.of(value);
        }
    }

    protected static List<Arg> valueToArgList(Object value) {
        List<?> objects = valueToCollection(value);
        List<Arg> args = new ArrayList<>(objects.size());
        for (Object datum : objects) {
            args.add(valueToArg(datum));
        }
        return args;
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

    /**
     * 添加条件
     */
    public SonBuilder addCondition(boolean sure, LogicalOperator logicalOperator, Arg arg, Operator operator,
                                   Object value) {
        if (sure) {
            if (value == null) {
                this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, Operator.isNull));
            } else {
                if (operator == Operator.in || operator == Operator.notIn) {
                    List<Arg> args = valueToArgList(value);
                    this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, operator, args));
                } else {
                    this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, operator, valueToArg(value)));
                }
            }
        }
        return this.sonBuilder;
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(LogicalOperator logicalOperator, Arg arg, Operator operator,
                                   Object value) {
        return this.addCondition(true, logicalOperator, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(boolean sure, LogicalOperator logicalOperator, Arg arg, Object value) {
        return this.addCondition(sure, logicalOperator, arg, Operator.eq, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(LogicalOperator logicalOperator, Arg arg, Object value) {
        return this.addCondition(true, logicalOperator, arg, Operator.eq, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(boolean sure, Arg arg, Operator operator, Object value) {
        return this.addCondition(sure, LogicalOperator.AND, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(Arg arg, Operator operator, Object value) {
        return this.addCondition(true, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(Arg arg, Object value) {
        return this.addCondition(arg, Operator.eq, value);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(boolean sure, LogicalOperator logicalOperator, Arg arg) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, Operator.isNull));
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(boolean sure, Arg arg) {
        return this.addIsNullCondition(sure, LogicalOperator.AND, arg);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(LogicalOperator logicalOperator, Arg arg) {
        return this.addIsNullCondition(true, logicalOperator, arg);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(Arg arg) {
        return this.addIsNullCondition(true, LogicalOperator.AND, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, Arg arg) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, Operator.isNotNull));
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(boolean sure, Arg arg) {
        return this.addIsNotNullCondition(sure, LogicalOperator.AND, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(LogicalOperator logicalOperator, Arg arg) {
        return this.addIsNotNullCondition(true, logicalOperator, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(Arg arg) {
        return this.addIsNotNullCondition(true, LogicalOperator.AND, arg);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(boolean sure, LogicalOperator logicalOperator, Arg arg, Arg minValue,
                                     Arg maxValue) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, Operator.between, minValue, maxValue));
        }
        return this.sonBuilder;
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(boolean sure, Arg arg, Arg minValue, Arg maxValue) {
        return this.addBtCondition(sure, LogicalOperator.AND, arg, minValue, maxValue);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(LogicalOperator logicalOperator, Arg arg, Arg minValue, Arg maxValue) {
        return this.addBtCondition(true, logicalOperator, arg, minValue, maxValue);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(Arg arg, Arg minValue, Arg maxValue) {
        return this.addBtCondition(true, LogicalOperator.AND, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(boolean sure, LogicalOperator logicalOperator, Arg arg, Arg minValue,
                                        Arg maxValue) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(logicalOperator, arg, Operator.notBetween, minValue,
                    maxValue));
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(boolean sure, Arg arg, Arg minValue, Arg maxValue) {
        return this.addNotBtCondition(sure, LogicalOperator.AND, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(LogicalOperator logicalOperator, Arg arg, Arg minValue, Arg maxValue) {
        return this.addNotBtCondition(true, logicalOperator, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(Arg arg, Arg minValue, Arg maxValue) {
        return this.addNotBtCondition(true, LogicalOperator.AND, arg, minValue, maxValue);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table,
                                              String field) {
        return this.addIsNullCondition(sure, LogicalOperator.AND, FieldArg.of(table, field));
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(true, LogicalOperator.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(EntityTable table, String field) {
        return this.addFieldIsNullCondition(true, LogicalOperator.AND, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, EntityTable table, String field) {
        return this.addFieldIsNullCondition(sure, LogicalOperator.AND, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(LogicalOperator logicalOperator, EntityTable table, String field) {
        return this.addFieldIsNullCondition(true, logicalOperator, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(true, logicalOperator, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(sure, logicalOperator, (EntityTable) this.table, field);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, LogicalOperator logicalOperator, Table table,
                                               String column) {
        return this.addIsNullCondition(sure, LogicalOperator.AND, ColumnArg.of(table, column));
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(String column) {
        return this.addColumnIsNullCondition(true, LogicalOperator.AND, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(Table table, String column) {
        return this.addColumnIsNullCondition(true, LogicalOperator.AND, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, String column) {
        return this.addColumnIsNullCondition(sure, LogicalOperator.AND, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, Table table, String column) {
        return this.addColumnIsNullCondition(sure, LogicalOperator.AND, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(LogicalOperator logicalOperator, Table table, String column) {
        return this.addColumnIsNullCondition(true, logicalOperator, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(LogicalOperator logicalOperator, String column) {
        return this.addColumnIsNullCondition(true, logicalOperator, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, LogicalOperator logicalOperator, String column) {
        return this.addColumnIsNullCondition(sure, logicalOperator, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, Table table,
                                                  String column) {
        return this.addIsNotNullCondition(sure, LogicalOperator.AND, ColumnArg.of(table, column));
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(String column) {
        return this.addColumnIsNotNullCondition(true, LogicalOperator.AND, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(Table table, String column) {
        return this.addColumnIsNotNullCondition(true, LogicalOperator.AND, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, String column) {
        return this.addColumnIsNotNullCondition(sure, LogicalOperator.AND, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, Table table, String column) {
        return this.addColumnIsNotNullCondition(sure, LogicalOperator.AND, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(LogicalOperator logicalOperator, Table table, String column) {
        return this.addColumnIsNotNullCondition(true, logicalOperator, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(LogicalOperator logicalOperator, String column) {
        return this.addColumnIsNotNullCondition(true, logicalOperator, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, String column) {
        return this.addColumnIsNotNullCondition(sure, logicalOperator, this.table, column);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table, String field,
                                        Operator operator, Object value) {
        return this.addCondition(sure, logicalOperator, FieldArg.of(table, field), operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field,
                                        Operator operator, Object value) {
        return this.addFieldCondition(true, logicalOperator, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, String field, Operator operator,
                                        Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, logicalOperator, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field, Operator operator,
                                        Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, logicalOperator, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, logicalOperator, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field,
                                        Object value) {
        return this.addFieldCondition(true, logicalOperator, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, logicalOperator, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table, String field,
                                        Object value) {
        return this.addFieldCondition(sure, logicalOperator, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, LogicalOperator.AND, (EntityTable) this.table, field, Operator.eq,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Object value) {
        return this.addFieldCondition(true, LogicalOperator.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Object value) {
        return this.addFieldCondition(sure, LogicalOperator.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Operator operator, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, LogicalOperator.AND, (EntityTable) this.table, field, operator,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Operator operator, Object value) {
        return this.addFieldCondition(true, LogicalOperator.AND, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Operator operator,
                                        Object value) {
        return this.addFieldCondition(sure, LogicalOperator.AND, table, field, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, Table table, String column,
                                         Operator operator, Object value) {
        return this.addCondition(sure, logicalOperator, ColumnArg.of(table, column), operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, Table table, String column,
                                         Operator operator, Object value) {
        return this.addColumnCondition(true, logicalOperator, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(true, logicalOperator, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(sure, logicalOperator, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, String column, Object value) {
        return this.addColumnCondition(true, logicalOperator, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(LogicalOperator logicalOperator, Table table, String column,
                                         Object value) {
        return this.addColumnCondition(true, logicalOperator, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, String column, Object value) {
        return this.addColumnCondition(sure, logicalOperator, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, LogicalOperator logicalOperator, Table table, String column,
                                         Object value) {
        return this.addColumnCondition(sure, logicalOperator, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Object value) {
        return this.addColumnCondition(true, LogicalOperator.AND, this.table, column, Operator.eq,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Object value) {
        return this.addColumnCondition(true, LogicalOperator.AND, table, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Object value) {
        return this.addColumnCondition(sure, LogicalOperator.AND, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Object value) {
        return this.addColumnCondition(sure, LogicalOperator.AND, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Operator operator, Object value) {
        return this.addColumnCondition(true, LogicalOperator.AND, this.table, column, operator,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Operator operator, Object value) {
        return this.addColumnCondition(true, LogicalOperator.AND, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Object value) {
        return this.addColumnCondition(sure, LogicalOperator.AND, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(sure, LogicalOperator.AND, table, column, operator, value);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table,
                                                 String field) {
        return this.addIsNotNullCondition(sure, logicalOperator, FieldArg.of(table, field));
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(true, LogicalOperator.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(true, LogicalOperator.AND, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(sure, LogicalOperator.AND, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(LogicalOperator logicalOperator, EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(true, logicalOperator, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(true, logicalOperator, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(sure, logicalOperator, (EntityTable) this.table, field);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table,
                                          String field, Object minValue, Object maxValue) {
        return this.addBtCondition(sure, logicalOperator, FieldArg.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(LogicalOperator logicalOperator, String field, Object minValue,
                                          Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, logicalOperator, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(LogicalOperator logicalOperator, EntityTable table, String field,
                                          Object minValue, Object maxValue) {
        return this.addFieldBtCondition(true, logicalOperator, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, LogicalOperator logicalOperator, String field, Object minValue,
                                          Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, logicalOperator, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, LogicalOperator.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        return this.addFieldBtCondition(true, LogicalOperator.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                          Object maxValue) {
        return this.addFieldBtCondition(sure, LogicalOperator.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, LogicalOperator logicalOperator, EntityTable table,
                                             String field, Object minValue, Object maxValue) {
        return this.addNotBtCondition(sure, logicalOperator, FieldArg.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(LogicalOperator logicalOperator, String field, Object minValue,
                                             Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, logicalOperator, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(LogicalOperator logicalOperator, EntityTable table, String field,
                                             Object minValue, Object maxValue) {
        return this.addFieldNotBtCondition(true, logicalOperator, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, LogicalOperator logicalOperator, String field, Object minValue,
                                             Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, logicalOperator, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, LogicalOperator.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        return this.addFieldNotBtCondition(true, LogicalOperator.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, LogicalOperator.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                             Object maxValue) {
        return this.addFieldNotBtCondition(sure, LogicalOperator.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, LogicalOperator logicalOperator, Table table,
                                           String column, Object minValue, Object maxValue) {
        return this.addBtCondition(sure, logicalOperator, ColumnArg.of(table, column), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(LogicalOperator logicalOperator, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(true, logicalOperator, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(LogicalOperator logicalOperator, Table table, String column,
                                           Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, logicalOperator, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, LogicalOperator logicalOperator, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(sure, logicalOperator, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, LogicalOperator.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(Table table, String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, LogicalOperator.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(sure, LogicalOperator.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Table table, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(sure, LogicalOperator.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, LogicalOperator logicalOperator, Table table,
                                              String column, Object minValue, Object maxValue) {
        return this.addNotBtCondition(sure, logicalOperator, ColumnArg.of(table, column), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(LogicalOperator logicalOperator, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(true, logicalOperator, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(LogicalOperator logicalOperator, Table table, String column,
                                              Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, logicalOperator, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, LogicalOperator logicalOperator, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(sure, logicalOperator, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, LogicalOperator.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Table table, String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, LogicalOperator.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(sure, LogicalOperator.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Table table, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(sure, LogicalOperator.AND, table, column, minValue, maxValue);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addCondition(sure, logicalOperator, FieldArg.of(leftTable, leftField), operator,
                FieldArg.of(rightTable, rightField));
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, EntityTable leftTable, String leftField,
                                               Operator operator, EntityTable rightTable, String rightField) {
        return this.addFieldCompareCondition(sure, LogicalOperator.AND, leftTable, leftField, operator, rightTable,
                rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addFieldCompareCondition(true, logicalOperator, leftTable, leftField, operator, rightTable,
                rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                               EntityTable rightTable, String rightField) {
        return this.addFieldCompareCondition(true, LogicalOperator.AND, leftTable, leftField, operator,
                rightTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, String leftField, Operator operator,
                                               String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, logicalOperator, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, LogicalOperator.AND, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, LogicalOperator.AND, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, LogicalOperator.AND, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(LogicalOperator logicalOperator, String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, logicalOperator, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftField,
                                               String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, logicalOperator, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, LogicalOperator.AND, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftField,
                                               Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, logicalOperator, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, Table leftTable,
                                                String leftColumn, Operator operator, Table rightTable,
                                                String rightColumn) {
        return this.addCondition(sure, logicalOperator, ColumnArg.of(leftTable, leftColumn), operator,
                ColumnArg.of(rightTable, rightColumn));
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, Table leftTable, String leftColumn,
                                                Operator operator, Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(sure, LogicalOperator.AND, leftTable, leftColumn, operator, rightTable,
                rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, Table leftTable,
                                                String leftColumn, Operator operator, Table rightTable,
                                                String rightColumn) {
        return this.addColumnCompareCondition(true, logicalOperator, leftTable, leftColumn, operator, rightTable,
                rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(Table leftTable, String leftColumn, Operator operator,
                                                Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(true, LogicalOperator.AND, leftTable, leftColumn, operator,
                rightTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, String leftColumn, Operator operator,
                                                String rightColumn) {
        return this.addColumnCompareCondition(true, logicalOperator, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(true, LogicalOperator.AND, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(true, LogicalOperator.AND, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(sure, LogicalOperator.AND, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(LogicalOperator logicalOperator, String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(true, logicalOperator, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftColumn,
                                                String rightColumn) {

        return this.addColumnCompareCondition(sure, logicalOperator, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(sure, LogicalOperator.AND, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, LogicalOperator logicalOperator, String leftColumn,
                                                Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(sure, logicalOperator, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
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
