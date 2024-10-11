package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

import java.lang.reflect.Array;
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

    protected static Collection<?> valueToCollection(Object obj) {
        if (obj instanceof Collection) {
            return (Collection<?>) obj;
        } else if (obj.getClass().isArray()) {
            //原始类型数组, 比如int[]
            if (obj.getClass().getComponentType().isPrimitive()) {
                int length = Array.getLength(obj);
                List<Object> ret = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    Object element = Array.get(obj, i);
                    ret.add(element);
                }
                return ret;
            }
            return Arrays.asList((Object[]) obj);
        } else {
            return Collections.singletonList(obj);
        }
    }

    protected static Operand valueToArg(Object value) {
        return Operand.objToOperand(value);
    }

    protected static List<Operand> valueToArgList(Object value) {
        Collection<?> objects = valueToCollection(value);
        List<Operand> args = new ArrayList<>(objects.size());
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
    public SonBuilder addCondition(boolean sure, AndOr andOr, Operand arg, Operator operator,
                                   Object value) {
        if (sure) {
            if (value == null) {
                this.conditions.add(new ArgCompareArgCondition(andOr, arg, Operator.isNull));
            } else {
                if (operator == Operator.in || operator == Operator.notIn) {
                    List<Operand> args = valueToArgList(value);
                    this.conditions.add(new ArgCompareArgCondition(andOr, arg, operator, args));
                } else {
                    this.conditions.add(new ArgCompareArgCondition(andOr, arg, operator, valueToArg(value)));
                }
            }
        }
        return this.sonBuilder;
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(AndOr andOr, Operand arg, Operator operator,
                                   Object value) {
        return this.addCondition(true, andOr, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(boolean sure, AndOr andOr, Operand arg, Object value) {
        return this.addCondition(sure, andOr, arg, Operator.eq, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(AndOr andOr, Operand arg, Object value) {
        return this.addCondition(true, andOr, arg, Operator.eq, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(boolean sure, Operand arg, Operator operator, Object value) {
        return this.addCondition(sure, AndOr.AND, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(Operand arg, Operator operator, Object value) {
        return this.addCondition(true, arg, operator, value);
    }

    /**
     * 添加条件
     */
    public SonBuilder addCondition(Operand arg, Object value) {
        return this.addCondition(arg, Operator.eq, value);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(boolean sure, AndOr andOr, Operand arg) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(andOr, arg, Operator.isNull));
        }
        return this.sonBuilder;
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(boolean sure, Operand arg) {
        return this.addIsNullCondition(sure, AndOr.AND, arg);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(AndOr andOr, Operand arg) {
        return this.addIsNullCondition(true, andOr, arg);
    }

    /**
     * 添加is null条件
     */
    public SonBuilder addIsNullCondition(Operand arg) {
        return this.addIsNullCondition(true, AndOr.AND, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(boolean sure, AndOr andOr, Operand arg) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(andOr, arg, Operator.isNotNull));
        }
        return this.sonBuilder;
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(boolean sure, Operand arg) {
        return this.addIsNotNullCondition(sure, AndOr.AND, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(AndOr andOr, Operand arg) {
        return this.addIsNotNullCondition(true, andOr, arg);
    }

    /**
     * 添加is not null条件
     */
    public SonBuilder addIsNotNullCondition(Operand arg) {
        return this.addIsNotNullCondition(true, AndOr.AND, arg);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(boolean sure, AndOr andOr, Operand arg, Operand minValue,
                                     Operand maxValue) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(andOr, arg, Operator.between, minValue, maxValue));
        }
        return this.sonBuilder;
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(boolean sure, Operand arg, Operand minValue, Operand maxValue) {
        return this.addBtCondition(sure, AndOr.AND, arg, minValue, maxValue);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(AndOr andOr, Operand arg, Operand minValue, Operand maxValue) {
        return this.addBtCondition(true, andOr, arg, minValue, maxValue);
    }

    /**
     * 添加between条件
     */
    public SonBuilder addBtCondition(Operand arg, Operand minValue, Operand maxValue) {
        return this.addBtCondition(true, AndOr.AND, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(boolean sure, AndOr andOr, Operand arg, Operand minValue,
                                        Operand maxValue) {
        if (sure) {
            this.conditions.add(new ArgCompareArgCondition(andOr, arg, Operator.notBetween, minValue,
                    maxValue));
        }
        return this.sonBuilder;
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(boolean sure, Operand arg, Operand minValue, Operand maxValue) {
        return this.addNotBtCondition(sure, AndOr.AND, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(AndOr andOr, Operand arg, Operand minValue, Operand maxValue) {
        return this.addNotBtCondition(true, andOr, arg, minValue, maxValue);
    }

    /**
     * 添加not between条件
     */
    public SonBuilder addNotBtCondition(Operand arg, Operand minValue, Operand maxValue) {
        return this.addNotBtCondition(true, AndOr.AND, arg, minValue, maxValue);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, AndOr andOr, EntityTable table,
                                              String field) {
        return this.addIsNullCondition(sure, andOr, EntityField.of(table, field));
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(true, AndOr.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(EntityTable table, String field) {
        return this.addFieldIsNullCondition(true, AndOr.AND, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(sure, AndOr.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, EntityTable table, String field) {
        return this.addFieldIsNullCondition(sure, AndOr.AND, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(AndOr andOr, EntityTable table, String field) {
        return this.addFieldIsNullCondition(true, andOr, table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(AndOr andOr, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(true, andOr, (EntityTable) this.table, field);
    }

    /**
     * 添加field is null条件
     */
    public SonBuilder addFieldIsNullCondition(boolean sure, AndOr andOr, String field) {
        this.checkEntityTable();
        return this.addFieldIsNullCondition(sure, andOr, (EntityTable) this.table, field);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, AndOr andOr, Table table,
                                               String column) {
        return this.addIsNullCondition(sure, andOr, TableColumn.of(table, column));
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(String column) {
        return this.addColumnIsNullCondition(true, AndOr.AND, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(Table table, String column) {
        return this.addColumnIsNullCondition(true, AndOr.AND, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, String column) {
        return this.addColumnIsNullCondition(sure, AndOr.AND, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, Table table, String column) {
        return this.addColumnIsNullCondition(sure, AndOr.AND, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(AndOr andOr, Table table, String column) {
        return this.addColumnIsNullCondition(true, andOr, table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(AndOr andOr, String column) {
        return this.addColumnIsNullCondition(true, andOr, this.table, column);
    }

    /**
     * 添加column is null条件
     */
    public SonBuilder addColumnIsNullCondition(boolean sure, AndOr andOr, String column) {
        return this.addColumnIsNullCondition(sure, andOr, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, AndOr andOr, Table table,
                                                  String column) {
        return this.addIsNotNullCondition(sure, andOr, TableColumn.of(table, column));
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(String column) {
        return this.addColumnIsNotNullCondition(true, AndOr.AND, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(Table table, String column) {
        return this.addColumnIsNotNullCondition(true, AndOr.AND, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, String column) {
        return this.addColumnIsNotNullCondition(sure, AndOr.AND, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, Table table, String column) {
        return this.addColumnIsNotNullCondition(sure, AndOr.AND, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(AndOr andOr, Table table, String column) {
        return this.addColumnIsNotNullCondition(true, andOr, table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(AndOr andOr, String column) {
        return this.addColumnIsNotNullCondition(true, andOr, this.table, column);
    }

    /**
     * 添加column is not null条件
     */
    public SonBuilder addColumnIsNotNullCondition(boolean sure, AndOr andOr, String column) {
        return this.addColumnIsNotNullCondition(sure, andOr, this.table, column);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, EntityTable table, String field,
                                        Operator operator, Object value) {
        return this.addCondition(sure, andOr, EntityField.of(table, field), operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, EntityTable table, String field,
                                        Operator operator, Object value) {
        return this.addFieldCondition(true, andOr, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, String field, Operator operator,
                                        Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, andOr, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, String field, Operator operator,
                                        Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, andOr, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, andOr, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, EntityTable table, String field,
                                        Object value) {
        return this.addFieldCondition(true, andOr, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, andOr, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, EntityTable table, String field,
                                        Object value) {
        return this.addFieldCondition(sure, andOr, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, AndOr.AND, (EntityTable) this.table, field, Operator.eq,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Object value) {
        return this.addFieldCondition(true, AndOr.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, AndOr.AND, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Object value) {
        return this.addFieldCondition(sure, AndOr.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Operator operator, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, AndOr.AND, (EntityTable) this.table, field, operator,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Operator operator, Object value) {
        return this.addFieldCondition(true, AndOr.AND, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Object value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, AndOr.AND, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Operator operator,
                                        Object value) {
        return this.addFieldCondition(sure, AndOr.AND, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, EntityTable table, String field,
                                        Operator operator, Operand value) {
        return this.addCondition(sure, andOr, EntityField.of(table, field), operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, EntityTable table, String field,
                                        Operator operator, Operand value) {
        return this.addFieldCondition(true, andOr, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, String field, Operator operator,
                                        Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, andOr, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, String field, Operator operator,
                                        Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, andOr, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, String field, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, andOr, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(AndOr andOr, EntityTable table, String field,
                                        Operand value) {
        return this.addFieldCondition(true, andOr, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, String field, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, andOr, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, AndOr andOr, EntityTable table, String field,
                                        Operand value) {
        return this.addFieldCondition(sure, andOr, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, AndOr.AND, (EntityTable) this.table, field, Operator.eq,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Operand value) {
        return this.addFieldCondition(true, AndOr.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, AndOr.AND, (EntityTable) this.table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Operand value) {
        return this.addFieldCondition(sure, AndOr.AND, table, field, Operator.eq, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(String field, Operator operator, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(true, AndOr.AND, (EntityTable) this.table, field, operator,
                value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(EntityTable table, String field, Operator operator, Operand value) {
        return this.addFieldCondition(true, AndOr.AND, table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, String field, Operator operator, Operand value) {
        this.checkEntityTable();
        return this.addFieldCondition(sure, AndOr.AND, (EntityTable) this.table, field, operator, value);
    }

    /**
     * 添加field条件
     */
    public SonBuilder addFieldCondition(boolean sure, EntityTable table, String field, Operator operator,
                                        Operand value) {
        return this.addFieldCondition(sure, AndOr.AND, table, field, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, Table table, String column,
                                         Operator operator, Object value) {
        return this.addCondition(sure, andOr, TableColumn.of(table, column), operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, Table table, String column,
                                         Operator operator, Object value) {
        return this.addColumnCondition(true, andOr, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(true, andOr, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(sure, andOr, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, String column, Object value) {
        return this.addColumnCondition(true, andOr, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, Table table, String column,
                                         Object value) {
        return this.addColumnCondition(true, andOr, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, String column, Object value) {
        return this.addColumnCondition(sure, andOr, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, Table table, String column,
                                         Object value) {
        return this.addColumnCondition(sure, andOr, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Object value) {
        return this.addColumnCondition(true, AndOr.AND, this.table, column, Operator.eq,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Object value) {
        return this.addColumnCondition(true, AndOr.AND, table, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Object value) {
        return this.addColumnCondition(sure, AndOr.AND, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Object value) {
        return this.addColumnCondition(sure, AndOr.AND, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Operator operator, Object value) {
        return this.addColumnCondition(true, AndOr.AND, this.table, column, operator,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Operator operator, Object value) {
        return this.addColumnCondition(true, AndOr.AND, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Object value) {
        return this.addColumnCondition(sure, AndOr.AND, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Operator operator,
                                         Object value) {
        return this.addColumnCondition(sure, AndOr.AND, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, Table table, String column,
                                         Operator operator, Operand value) {
        return this.addCondition(sure, andOr, TableColumn.of(table, column), operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, Table table, String column,
                                         Operator operator, Operand value) {
        return this.addColumnCondition(true, andOr, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, String column, Operator operator,
                                         Operand value) {
        return this.addColumnCondition(true, andOr, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, String column, Operator operator,
                                         Operand value) {
        return this.addColumnCondition(sure, andOr, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, String column, Operand value) {
        return this.addColumnCondition(true, andOr, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(AndOr andOr, Table table, String column,
                                         Operand value) {
        return this.addColumnCondition(true, andOr, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, String column, Operand value) {
        return this.addColumnCondition(sure, andOr, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, AndOr andOr, Table table, String column,
                                         Operand value) {
        return this.addColumnCondition(sure, andOr, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Operand value) {
        return this.addColumnCondition(true, AndOr.AND, this.table, column, Operator.eq,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Operand value) {
        return this.addColumnCondition(true, AndOr.AND, table, column, Operator.eq, value);
    }

    public SonBuilder addColumnCondition(boolean sure, String column, Operand value) {
        return this.addColumnCondition(sure, AndOr.AND, this.table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Operand value) {
        return this.addColumnCondition(sure, AndOr.AND, table, column, Operator.eq, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(String column, Operator operator, Operand value) {
        return this.addColumnCondition(true, AndOr.AND, this.table, column, operator,
                value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(Table table, String column, Operator operator, Operand value) {
        return this.addColumnCondition(true, AndOr.AND, table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, String column, Operator operator, Operand value) {
        return this.addColumnCondition(sure, AndOr.AND, this.table, column, operator, value);
    }

    /**
     * 添加column条件
     */
    public SonBuilder addColumnCondition(boolean sure, Table table, String column, Operator operator,
                                         Operand value) {
        return this.addColumnCondition(sure, AndOr.AND, table, column, operator, value);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, AndOr andOr, EntityTable table,
                                                 String field) {
        return this.addIsNotNullCondition(sure, andOr, EntityField.of(table, field));
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(true, AndOr.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(true, AndOr.AND, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(sure, AndOr.AND, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(sure, AndOr.AND, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(AndOr andOr, EntityTable table, String field) {
        return this.addFieldIsNotNullCondition(true, andOr, table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(AndOr andOr, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(true, andOr, (EntityTable) this.table, field);
    }

    /**
     * 添加field is not null条件
     */
    public SonBuilder addFieldIsNotNullCondition(boolean sure, AndOr andOr, String field) {
        this.checkEntityTable();
        return this.addFieldIsNotNullCondition(sure, andOr, (EntityTable) this.table, field);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, AndOr andOr, EntityTable table,
                                          String field, Object minValue, Object maxValue) {
        return this.addBtCondition(sure, andOr, EntityField.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(AndOr andOr, String field, Object minValue,
                                          Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, andOr, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(AndOr andOr, EntityTable table, String field,
                                          Object minValue, Object maxValue) {
        return this.addFieldBtCondition(true, andOr, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, AndOr andOr, String field, Object minValue,
                                          Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, andOr, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        return this.addFieldBtCondition(true, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                          Object maxValue) {
        return this.addFieldBtCondition(sure, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, AndOr andOr, EntityTable table,
                                          String field, Operand minValue, Operand maxValue) {
        return this.addBtCondition(sure, andOr, EntityField.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(AndOr andOr, String field, Operand minValue,
                                          Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, andOr, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(AndOr andOr, EntityTable table, String field,
                                          Operand minValue, Operand maxValue) {
        return this.addFieldBtCondition(true, andOr, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, AndOr andOr, String field, Operand minValue,
                                          Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, andOr, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(String field, Operand minValue, Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(true, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(EntityTable table, String field, Operand minValue, Operand maxValue) {
        return this.addFieldBtCondition(true, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, String field, Operand minValue, Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldBtCondition(sure, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field between on条件
     */
    public SonBuilder addFieldBtCondition(boolean sure, EntityTable table, String field, Operand minValue,
                                          Operand maxValue) {
        return this.addFieldBtCondition(sure, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, AndOr andOr, EntityTable table,
                                             String field, Object minValue, Object maxValue) {
        return this.addNotBtCondition(sure, andOr, EntityField.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(AndOr andOr, String field, Object minValue,
                                             Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, andOr, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(AndOr andOr, EntityTable table, String field,
                                             Object minValue, Object maxValue) {
        return this.addFieldNotBtCondition(true, andOr, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, AndOr andOr, String field, Object minValue,
                                             Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, andOr, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(EntityTable table, String field, Object minValue, Object maxValue) {
        return this.addFieldNotBtCondition(true, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, String field, Object minValue, Object maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, EntityTable table, String field, Object minValue,
                                             Object maxValue) {
        return this.addFieldNotBtCondition(sure, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, AndOr andOr, EntityTable table,
                                             String field, Operand minValue, Operand maxValue) {
        return this.addNotBtCondition(sure, andOr, EntityField.of(table, field), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(AndOr andOr, String field, Operand minValue,
                                             Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, andOr, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(AndOr andOr, EntityTable table, String field,
                                             Operand minValue, Operand maxValue) {
        return this.addFieldNotBtCondition(true, andOr, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, AndOr andOr, String field, Operand minValue,
                                             Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, andOr, (EntityTable) this.table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(String field, Operand minValue, Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(true, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(EntityTable table, String field, Operand minValue, Operand maxValue) {
        return this.addFieldNotBtCondition(true, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, String field, Operand minValue, Operand maxValue) {
        this.checkEntityTable();
        return this.addFieldNotBtCondition(sure, AndOr.AND, (EntityTable) this.table, field, minValue,
                maxValue);
    }

    /**
     * 添加field not between on条件
     */
    public SonBuilder addFieldNotBtCondition(boolean sure, EntityTable table, String field, Operand minValue,
                                             Operand maxValue) {
        return this.addFieldNotBtCondition(sure, AndOr.AND, table, field, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, AndOr andOr, Table table,
                                           String column, Object minValue, Object maxValue) {
        return this.addBtCondition(sure, andOr, TableColumn.of(table, column), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(AndOr andOr, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(true, andOr, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(AndOr andOr, Table table, String column,
                                           Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, andOr, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, AndOr andOr, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(sure, andOr, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(Table table, String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(true, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, String column, Object minValue, Object maxValue) {
        return this.addColumnBtCondition(sure, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Table table, String column, Object minValue,
                                           Object maxValue) {
        return this.addColumnBtCondition(sure, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, AndOr andOr, Table table,
                                           String column, Operand minValue, Operand maxValue) {
        return this.addBtCondition(sure, andOr, TableColumn.of(table, column), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(AndOr andOr, String column, Operand minValue,
                                           Operand maxValue) {
        return this.addColumnBtCondition(true, andOr, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(AndOr andOr, Table table, String column,
                                           Operand minValue, Operand maxValue) {
        return this.addColumnBtCondition(true, andOr, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, AndOr andOr, String column, Operand minValue,
                                           Operand maxValue) {
        return this.addColumnBtCondition(sure, andOr, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(String column, Operand minValue, Operand maxValue) {
        return this.addColumnBtCondition(true, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(Table table, String column, Operand minValue, Operand maxValue) {
        return this.addColumnBtCondition(true, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, String column, Operand minValue, Operand maxValue) {
        return this.addColumnBtCondition(sure, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column between on条件
     */
    public SonBuilder addColumnBtCondition(boolean sure, Table table, String column, Operand minValue,
                                           Operand maxValue) {
        return this.addColumnBtCondition(sure, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, AndOr andOr, Table table,
                                              String column, Object minValue, Object maxValue) {
        return this.addNotBtCondition(sure, andOr, TableColumn.of(table, column), valueToArg(minValue),
                valueToArg(maxValue));
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(AndOr andOr, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(true, andOr, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(AndOr andOr, Table table, String column,
                                              Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, andOr, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, AndOr andOr, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(sure, andOr, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Table table, String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(true, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, String column, Object minValue, Object maxValue) {
        return this.addColumnNotBtCondition(sure, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Table table, String column, Object minValue,
                                              Object maxValue) {
        return this.addColumnNotBtCondition(sure, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(AndOr andOr, String column, Operand minValue,
                                              Operand maxValue) {
        return this.addColumnNotBtCondition(true, andOr, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(AndOr andOr, Table table, String column,
                                              Operand minValue, Operand maxValue) {
        return this.addColumnNotBtCondition(true, andOr, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, AndOr andOr, String column, Operand minValue,
                                              Operand maxValue) {
        return this.addColumnNotBtCondition(sure, andOr, this.table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(String column, Operand minValue, Operand maxValue) {
        return this.addColumnNotBtCondition(true, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(Table table, String column, Operand minValue, Operand maxValue) {
        return this.addColumnNotBtCondition(true, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, String column, Operand minValue, Operand maxValue) {
        return this.addColumnNotBtCondition(sure, AndOr.AND, this.table, column, minValue,
                maxValue);
    }

    /**
     * 添加column not between on条件
     */
    public SonBuilder addColumnNotBtCondition(boolean sure, Table table, String column, Operand minValue,
                                              Operand maxValue) {
        return this.addColumnNotBtCondition(sure, AndOr.AND, table, column, minValue, maxValue);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, AndOr andOr, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addCondition(sure, andOr, EntityField.of(leftTable, leftField), operator,
                EntityField.of(rightTable, rightField));
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, EntityTable leftTable, String leftField,
                                               Operator operator, EntityTable rightTable, String rightField) {
        return this.addFieldCompareCondition(sure, AndOr.AND, leftTable, leftField, operator, rightTable,
                rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(AndOr andOr, EntityTable leftTable,
                                               String leftField, Operator operator, EntityTable rightTable,
                                               String rightField) {
        return this.addFieldCompareCondition(true, andOr, leftTable, leftField, operator, rightTable,
                rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                               EntityTable rightTable, String rightField) {
        return this.addFieldCompareCondition(true, AndOr.AND, leftTable, leftField, operator,
                rightTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(AndOr andOr, String leftField, Operator operator,
                                               String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, andOr, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, AndOr.AND, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, AndOr.AND, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, AndOr.AND, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(AndOr andOr, String leftField, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(true, andOr, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, AndOr andOr, String leftField,
                                               String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, andOr, (EntityTable) this.table, leftField,
                Operator.eq, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, String leftField, Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, AndOr.AND, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对field比条件
     */
    public SonBuilder addFieldCompareCondition(boolean sure, AndOr andOr, String leftField,
                                               Operator operator, String rightField) {
        this.checkAllEntityTable();
        return this.addFieldCompareCondition(sure, andOr, (EntityTable) this.table, leftField,
                operator, (EntityTable) this.otherTable, rightField);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, AndOr andOr, Table leftTable,
                                                String leftColumn, Operator operator, Table rightTable,
                                                String rightColumn) {
        return this.addCondition(sure, andOr, TableColumn.of(leftTable, leftColumn), operator,
                TableColumn.of(rightTable, rightColumn));
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, Table leftTable, String leftColumn,
                                                Operator operator, Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(sure, AndOr.AND, leftTable, leftColumn, operator, rightTable,
                rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(AndOr andOr, Table leftTable,
                                                String leftColumn, Operator operator, Table rightTable,
                                                String rightColumn) {
        return this.addColumnCompareCondition(true, andOr, leftTable, leftColumn, operator, rightTable,
                rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(Table leftTable, String leftColumn, Operator operator,
                                                Table rightTable, String rightColumn) {
        return this.addColumnCompareCondition(true, AndOr.AND, leftTable, leftColumn, operator,
                rightTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(AndOr andOr, String leftColumn, Operator operator,
                                                String rightColumn) {
        return this.addColumnCompareCondition(true, andOr, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(true, AndOr.AND, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(true, AndOr.AND, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(sure, AndOr.AND, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(AndOr andOr, String leftColumn, String rightColumn) {
        return this.addColumnCompareCondition(true, andOr, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, AndOr andOr, String leftColumn,
                                                String rightColumn) {

        return this.addColumnCompareCondition(sure, andOr, this.table, leftColumn,
                Operator.eq, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, String leftColumn, Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(sure, AndOr.AND, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添对column比条件
     */
    public SonBuilder addColumnCompareCondition(boolean sure, AndOr andOr, String leftColumn,
                                                Operator operator, String rightColumn) {
        return this.addColumnCompareCondition(sure, andOr, this.table, leftColumn,
                operator, this.otherTable, rightColumn);
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(boolean sure, AndOr andOr, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(andOr, sql));
        }
        return this.sonBuilder;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(AndOr andOr, String sql) {
        return this.addSqlCondition(true, andOr, sql);
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSqlCondition(boolean sure, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(AndOr.AND, sql));
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
     * 添exists条件
     */
    public SonBuilder exists(boolean sure, AndOr andOr, EzQuery<?> query) {
        if (sure) {
            this.conditions.add(new ExistsCondition(andOr, query, false));
        }
        return this.sonBuilder;
    }

    /**
     * 添exists条件
     */
    public SonBuilder exists(AndOr andOr, EzQuery<?> query) {
        return this.exists(true, andOr, query);
    }

    /**
     * 添exists条件
     */
    public SonBuilder exists(boolean sure, EzQuery<?> query) {
        return this.exists(sure, AndOr.AND, query);
    }

    /**
     * 添exists条件
     */
    public SonBuilder exists(EzQuery<?> query) {
        return this.exists(true, query);
    }

    /**
     * 添not exists条件
     */
    public SonBuilder notExists(boolean sure, AndOr andOr, EzQuery<?> query) {
        if (sure) {
            this.conditions.add(new ExistsCondition(andOr, query, true));
        }
        return this.sonBuilder;
    }

    /**
     * 添not exists条件
     */
    public SonBuilder notExists(AndOr andOr, EzQuery<?> query) {
        return this.notExists(true, andOr, query);
    }

    /**
     * 添not exists条件
     */
    public SonBuilder notExists(boolean sure, EzQuery<?> query) {
        return this.notExists(sure, AndOr.AND, query);
    }

    /**
     * 添not exists条件
     */
    public SonBuilder notExists(EzQuery<?> query) {
        return this.notExists(true, query);
    }
}
