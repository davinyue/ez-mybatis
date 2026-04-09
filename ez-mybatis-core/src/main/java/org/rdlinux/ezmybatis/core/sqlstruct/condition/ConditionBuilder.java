package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 条件构造器
 */
public class ConditionBuilder<SonBuilder extends ConditionBuilder<?>> {
    protected List<Condition> conditions;

    public ConditionBuilder(List<Condition> conditions) {
        this.conditions = conditions;
    }

    protected static Operand valueToArg(Object value) {
        return Operand.objToOperand(value);
    }

    /**
     * 添加条件
     */
    @SuppressWarnings("unchecked")
    public SonBuilder add(boolean sure, AndOr andOr, Operand leftOperand, Operator op, Object rightOperand) {
        if (sure) {
            if (rightOperand == null) {
                this.conditions.add(new ArgCompareArgCondition(andOr, leftOperand, Operator.isNull));
            } else {
                if (op == Operator.in || op == Operator.notIn) {
                    List<Operand> args = Operand.valueToArgList(rightOperand);
                    this.conditions.add(new ArgCompareArgCondition(andOr, leftOperand, op, args));
                } else {
                    this.conditions.add(new ArgCompareArgCondition(andOr, leftOperand, op, valueToArg(rightOperand)));
                }
            }
        }
        return (SonBuilder) this;
    }

    /**
     * 添加条件
     */
    public SonBuilder add(AndOr andOr, Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(Boolean.TRUE, andOr, leftOperand, op, rightOperand);
    }

    /**
     * 添加条件
     */
    public SonBuilder add(boolean sure, Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(sure, AndOr.AND, leftOperand, op, rightOperand);
    }


    /**
     * 添加条件
     */
    public SonBuilder add(boolean sure, Operand leftOperand, Object rightOperand) {
        return this.add(sure, AndOr.AND, leftOperand, Operator.eq, rightOperand);
    }

    /**
     * 添加条件
     */
    public SonBuilder add(Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(Boolean.TRUE, AndOr.AND, leftOperand, op, rightOperand);
    }


    /**
     * 添加条件
     */
    public SonBuilder add(Operand leftOperand, Object rightOperand) {
        return this.add(Boolean.TRUE, AndOr.AND, leftOperand, Operator.eq, rightOperand);
    }

    /**
     * 添加条件
     */
    @SuppressWarnings("unchecked")
    public SonBuilder add(boolean sure, Condition condition) {
        if (sure) {
            this.conditions.add(condition);
        }
        return (SonBuilder) this;
    }

    /**
     * 添加条件
     */
    @SuppressWarnings("unchecked")
    public SonBuilder add(Condition condition) {
        this.conditions.add(condition);
        return (SonBuilder) this;
    }

    /**
     * 组条件(Lambda 闭包)
     */
    @SuppressWarnings("unchecked")
    public SonBuilder addGroup(boolean sure, AndOr andOr, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        if (sure) {
            GroupCondition condition = new GroupCondition(Boolean.TRUE, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            ConditionBuilder sonBuilder = new ConditionBuilder(condition.getConditions());
            consumer.accept(sonBuilder);
        }
        return (SonBuilder) this;
    }

    /**
     * 组条件(Lambda 闭包)
     */
    public SonBuilder addGroup(AndOr andOr, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(Boolean.TRUE, andOr, consumer);
    }

    /**
     * 组条件(Lambda 闭包)
     */
    public SonBuilder addGroup(boolean sure, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(sure, AndOr.AND, consumer);
    }

    /**
     * 组条件(Lambda 闭包)
     */
    public SonBuilder addGroup(Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(Boolean.TRUE, consumer);
    }


    /**
     * 添sql条件
     */
    @SuppressWarnings("unchecked")
    public SonBuilder addSql(boolean sure, AndOr andOr, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(andOr, sql));
        }
        return (SonBuilder) this;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSql(AndOr andOr, String sql) {
        return this.addSql(true, andOr, sql);
    }

    /**
     * 添sql条件
     */
    @SuppressWarnings("unchecked")
    public SonBuilder addSql(boolean sure, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(AndOr.AND, sql));
        }
        return (SonBuilder) this;
    }

    /**
     * 添sql条件
     */
    public SonBuilder addSql(String sql) {
        return this.addSql(true, sql);
    }
}
