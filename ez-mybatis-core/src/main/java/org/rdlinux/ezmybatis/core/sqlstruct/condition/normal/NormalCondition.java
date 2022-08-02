package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
        if (this.getOperator() == Operator.in || this.getOperator() == Operator.notIn) {
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

    private Collection<?> valueToCollection() {
        if (this.value instanceof Collection) {
            Assert.isTrue(((Collection<?>) this.value).size() > 0,
                    "When using in query, the data cannot be empty");
            return (Collection<?>) this.value;
        } else if (this.value.getClass().isArray()) {
            Assert.isTrue(((Object[]) this.value).length > 0,
                    "When using in query, the data cannot be empty");
            return Arrays.asList((Object[]) this.value);
        } else {
            return Collections.singleton(this.value);
        }
    }

    private String inToSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = new StringBuilder(" ").append(this.getSqlField(configuration)).append(" ");
        Collection<?> valueCo = this.valueToCollection();
        if (valueCo.size() == 1) {
            Object sValue = valueCo.iterator().next();
            if (sValue instanceof EzQuery) {
                sql.append(this.getOperator().getOperator());
            } else if (this.getOperator() == Operator.in) {
                sql.append(Operator.eq.getOperator());
            } else {
                sql.append(Operator.ne.getOperator());
            }
            sql.append(" ").append(Condition.valueToSqlStruct(configuration, mybatisParamHolder, sValue))
                    .append(" ");
        } else {
            sql.append(this.getOperator().getOperator()).append(" (");
            int i = 0;
            for (Object valueItem : valueCo) {
                sql.append(Condition.valueToSqlStruct(configuration, mybatisParamHolder, valueItem));
                if (i + 1 < valueCo.size()) {
                    sql.append(", ");
                }
                i++;
            }
            sql.append(" ) ");
        }
        return sql.toString();
    }
}
