package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 条件构造器。
 *
 * @param <SonBuilder> 具体子构造器类型
 */
public class ConditionBuilder<SonBuilder extends ConditionBuilder<?>> {
    /**
     * 当前构造器维护的条件列表。
     */
    protected List<Condition> conditions;

    /**
     * 创建条件构造器。
     *
     * @param conditions 条件列表
     */
    public ConditionBuilder(List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * 将普通对象包装为操作数节点。
     *
     * @param value 普通对象
     * @return 操作数节点
     */
    protected static Operand valueToArg(Object value) {
        return Operand.objToOperand(value);
    }

    /**
     * 根据条件添加比较条件。
     *
     * @param sure         是否启用当前条件
     * @param andOr        条件连接方式
     * @param leftOperand  左操作数
     * @param op           比较运算符
     * @param rightOperand 右操作数
     * @return 当前构造器
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
     * 添加比较条件。
     *
     * @param andOr        条件连接方式
     * @param leftOperand  左操作数
     * @param op           比较运算符
     * @param rightOperand 右操作数
     * @return 当前构造器
     */
    public SonBuilder add(AndOr andOr, Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(Boolean.TRUE, andOr, leftOperand, op, rightOperand);
    }

    /**
     * 根据条件添加 AND 比较条件。
     *
     * @param sure         是否启用当前条件
     * @param leftOperand  左操作数
     * @param op           比较运算符
     * @param rightOperand 右操作数
     * @return 当前构造器
     */
    public SonBuilder add(boolean sure, Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(sure, AndOr.AND, leftOperand, op, rightOperand);
    }


    /**
     * 根据条件添加 AND 等值条件。
     *
     * @param sure         是否启用当前条件
     * @param leftOperand  左操作数
     * @param rightOperand 右操作数
     * @return 当前构造器
     */
    public SonBuilder add(boolean sure, Operand leftOperand, Object rightOperand) {
        return this.add(sure, AndOr.AND, leftOperand, Operator.eq, rightOperand);
    }

    /**
     * 添加 AND 比较条件。
     *
     * @param leftOperand  左操作数
     * @param op           比较运算符
     * @param rightOperand 右操作数
     * @return 当前构造器
     */
    public SonBuilder add(Operand leftOperand, Operator op, Object rightOperand) {
        return this.add(Boolean.TRUE, AndOr.AND, leftOperand, op, rightOperand);
    }


    /**
     * 添加 AND 等值条件。
     *
     * @param leftOperand  左操作数
     * @param rightOperand 右操作数
     * @return 当前构造器
     */
    public SonBuilder add(Operand leftOperand, Object rightOperand) {
        return this.add(Boolean.TRUE, AndOr.AND, leftOperand, Operator.eq, rightOperand);
    }

    /**
     * 根据条件添加已构造的条件对象。
     *
     * @param sure      是否启用当前条件
     * @param condition 条件对象
     * @return 当前构造器
     */
    @SuppressWarnings("unchecked")
    public SonBuilder add(boolean sure, Condition condition) {
        if (sure) {
            this.conditions.add(condition);
        }
        return (SonBuilder) this;
    }

    /**
     * 添加已构造的条件对象。
     *
     * @param condition 条件对象
     * @return 当前构造器
     */
    @SuppressWarnings("unchecked")
    public SonBuilder add(Condition condition) {
        this.conditions.add(condition);
        return (SonBuilder) this;
    }

    /**
     * 根据条件添加条件组。
     *
     * @param sure     是否启用当前条件组
     * @param andOr    条件连接方式
     * @param consumer 条件组构造回调
     * @return 当前构造器
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public SonBuilder addGroup(boolean sure, AndOr andOr, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        if (sure) {
            GroupCondition condition = new GroupCondition(Boolean.TRUE, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            ConditionBuilder sonBuilder = new ConditionBuilder<>(condition.getConditions());
            consumer.accept(sonBuilder);
        }
        return (SonBuilder) this;
    }

    /**
     * 添加条件组。
     *
     * @param andOr    条件连接方式
     * @param consumer 条件组构造回调
     * @return 当前构造器
     */
    public SonBuilder addGroup(AndOr andOr, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(Boolean.TRUE, andOr, consumer);
    }

    /**
     * 根据条件添加 AND 条件组。
     *
     * @param sure     是否启用当前条件组
     * @param consumer 条件组构造回调
     * @return 当前构造器
     */
    public SonBuilder addGroup(boolean sure, Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(sure, AndOr.AND, consumer);
    }

    /**
     * 添加 AND 条件组。
     *
     * @param consumer 条件组构造回调
     * @return 当前构造器
     */
    public SonBuilder addGroup(Consumer<ConditionBuilder<SonBuilder>> consumer) {
        return this.addGroup(Boolean.TRUE, consumer);
    }


    /**
     * 根据条件添加原生 SQL 条件。
     *
     * @param sure  是否启用当前条件
     * @param andOr 条件连接方式
     * @param sql   原生 SQL 条件片段
     * @return 当前构造器
     */
    @SuppressWarnings("unchecked")
    public SonBuilder addSql(boolean sure, AndOr andOr, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(andOr, sql));
        }
        return (SonBuilder) this;
    }

    /**
     * 添加原生 SQL 条件。
     *
     * @param andOr 条件连接方式
     * @param sql   原生 SQL 条件片段
     * @return 当前构造器
     */
    public SonBuilder addSql(AndOr andOr, String sql) {
        return this.addSql(true, andOr, sql);
    }

    /**
     * 根据条件添加 AND 原生 SQL 条件。
     *
     * @param sure 是否启用当前条件
     * @param sql  原生 SQL 条件片段
     * @return 当前构造器
     */
    @SuppressWarnings("unchecked")
    public SonBuilder addSql(boolean sure, String sql) {
        if (sure) {
            this.conditions.add(new SqlCondition(AndOr.AND, sql));
        }
        return (SonBuilder) this;
    }

    /**
     * 添加 AND 原生 SQL 条件。
     *
     * @param sql 原生 SQL 条件片段
     * @return 当前构造器
     */
    public SonBuilder addSql(String sql) {
        return this.addSql(true, sql);
    }
}
