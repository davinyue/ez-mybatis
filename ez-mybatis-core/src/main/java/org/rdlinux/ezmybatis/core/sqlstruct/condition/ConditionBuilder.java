package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

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

    protected static Operand valueToArg(Object value) {
        return Operand.objToOperand(value);
    }

    public ParentBuilder done() {
        return this.parentBuilder;
    }

    protected void checkEntityTable() {
        if (!(this.table instanceof EntityTable)) {
            throw new IllegalArgumentException("Only EntityTable is supported");
        }
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
                    List<Operand> args = Operand.valueToArgList(value);
                    this.conditions.add(new ArgCompareArgCondition(andOr, arg, operator, args));
                } else {
                    this.conditions.add(new ArgCompareArgCondition(andOr, arg, operator, valueToArg(value)));
                }
            }
        }
        return this.sonBuilder;
    }

    public SonBuilder addCondition(boolean sure, Condition condition) {
        if (sure) {
            this.conditions.add(condition);
        }
        return this.sonBuilder;
    }

    public SonBuilder addCondition(Condition condition) {
        this.conditions.add(condition);
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
