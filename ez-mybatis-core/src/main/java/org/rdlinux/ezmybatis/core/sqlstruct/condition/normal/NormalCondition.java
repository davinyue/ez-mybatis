package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

/**
 * 普通条件
 */
@Getter
public abstract class NormalCondition implements Condition {
    protected LogicalOperator logicalOperator;
    protected Operator operator;
    protected Object value;

    public NormalCondition(LogicalOperator logicalOperator, Operator operator, Object value) {
        if (logicalOperator == null) {
            logicalOperator = LogicalOperator.AND;
        }
        if (operator == Operator.between || operator == Operator.notBetween || operator == Operator.isNull ||
                operator == Operator.isNotNull) {
            throw new IllegalArgumentException("Unsupported operator");
        }
        Assert.notNull(value, "value can not be null");
        this.operator = operator;
        this.logicalOperator = logicalOperator;
        this.value = value;
    }

    protected abstract String getSqlField(Configuration configuration);

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        if (this.getOperator() == Operator.in) {
            return this.inToSqlPart(configuration, mybatisParamHolder);
        } else {
            return this.otherToSqlPart(configuration, mybatisParamHolder);
        }
    }

    private String otherToSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        return " " + this.getSqlField(configuration) + " " +
                this.getOperator().getOperator() + " " +
                Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.value) + " ";
    }

    private String inToSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(this.getSqlField(configuration)).append(" ")
                .append(this.getOperator().getOperator()).append(" ");
        sql.append("( ");
        if (this.value instanceof Collection) {
            int i = 0;
            for (Object valueItem : (Collection<?>) this.value) {
                sql.append(Condition.valueToSqlStruct(configuration, mybatisParamHolder, valueItem));
                if (i + 1 < ((Collection<?>) this.value).size()) {
                    sql.append(", ");
                }
                i++;
            }
        } else if (this.value.getClass().isArray()) {
            int i = 0;
            for (Object valueItem : (Object[]) this.value) {
                sql.append(Condition.valueToSqlStruct(configuration, mybatisParamHolder, valueItem));
                if (i + 1 < ((Object[]) this.value).length) {
                    sql.append(", ");
                }
                i++;
            }
        } else {
            sql.append(Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.value));
        }
        sql.append(" ) ");
        return sql.toString();
    }
}
